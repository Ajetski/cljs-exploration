(ns core
  (:require
   [compojure.core :refer [defroutes]]
   [compojure.route :as route]
   [db.setup]
   [ring.adapter.jetty :refer [run-jetty]]
   [router.todo :refer [todo-routes]]
   [utils :refer [render-page]]))

(def not-found-page
  [:h1 "Page not found"])

(defroutes app
  todo-routes
  (route/resources "/assets")
  (route/not-found (render-page not-found-page)))

(defn -main
  [& _args]
  (db.setup/create-tables!)
  (run-jetty app {:port 3000
                  :join? false}))

(comment
  (do
    ; for development, eval do block to restart server
    (.stop server)
    #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
    (def server (-main))))

