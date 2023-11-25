(ns core
  (:require
   [compojure.core :refer [defroutes]]
   [compojure.route :as route]
   [ring.adapter.jetty :refer [run-jetty]]
   [routers.home :refer [home-routes]]
   [routers.todo :refer [todo-routes]]
   [utils :refer [render]]))

(def not-found-page
  [:h1 "Page not found"])

(defroutes app
  home-routes
  todo-routes
  (route/resources "/assets")
  (route/not-found (render not-found-page)))

(defn -main
  [& _args]
  (run-jetty app {:port 3000
                  :join? false}))

(comment
  (do
    ; for development, eval do block to restart server
    (.stop server)
    #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
    (def server (-main))))

