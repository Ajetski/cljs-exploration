(ns clj.core
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [clojure.java.io :as io]
   [hiccup2.core :as h]))

(def cwd (-> (java.io.File. ".") .getAbsolutePath))

(def cljs-files ["form.cljs" "app.cljs"])

(def index-html
  [:html
   [:head
    [:script {:src "https://cdn.jsdelivr.net/npm/scittle@0.1.2/dist/scittle.js"}]
    [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/react/17.0.2/umd/react.production.min.js"}]
    [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/react-dom/17.0.2/umd/react-dom.production.min.js"}]
    [:script {:src "https://cdn.jsdelivr.net/npm/scittle@0.1.2/dist/scittle.reagent.js"}]]
   [:body
    [:div#app]
    (map (fn [file-name]
           [:script {:src (str "/assets/cljs/" file-name)
                     :type "application/x-scittle"}])
         cljs-files)]])

(defroutes app
  (GET "/" [] (str (h/html index-html)))
  (route/resources "/assets")
  (route/files "/assets/cljs" {:root (str cwd "/src/cljs/")})
  (route/files "/assets/cljc" {:root (str cwd "/src/cljc/")})
  (route/not-found "<h1>Page not found</h1>"))

(defn -main
  [& args]
  (run-jetty app {:port 3000
                  :join? false}))

(comment
  (do
    ; for development, eval do block to restart server
    (.stop server)
    #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
    (def server (-main))))
