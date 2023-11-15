(ns cljs.form
  (:require [reagent.core :as r]))

(defn ^:export input [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])

