(ns utils
  (:require [hiccup2.core :as h]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.string :as string]
            [ring.util.codec :refer [form-decode]]))

(defn root-template [body]
  [:html
   [:head
    (for [lib [{:name "htmx.org", :version "1.9.9"}
               {:name "hyperscript.org", :version "0.9.12"}]]
      [:script {:src (str "https://unpkg.com/" (:name lib) "@" (:version lib))}])
    [:link {:rel "stylesheet"
            :href "/assets/styles.css"}]]
   [:body body]])

(defn render*
  "renders hiccup vector or function component. does NOT wrap result in any template"
  [input & args]
  (str (h/html (if (vector? input)
                 input
                 (apply input args)))))

(defn render
  "renders hiccup vector or function component. wraps result in root template"
  [input & args]
  (str (h/html (root-template (if (vector? input)
                                input
                                (apply input args))))))

(defn style [s]
  (string/join ";" (map #(str (name %) ":" ((keyword %) s)) (keys s))))

(def extract-body
  (comp keywordize-keys form-decode slurp :body))

(defn extract-query [req]
  (let [query (:query-string req)]
    (if query
      ((comp keywordize-keys form-decode) query)
      nil)))
