(ns cljs.app
(:require [reagent.core :as r]
          [cljs.form :as form]))

(defn ^:export app []
  (let [val (r/atom "foo")]
    (fn []
      [:div
       [:p "The value is now: " @val]
       [:p "Change it here: " [form/input val]]])))

