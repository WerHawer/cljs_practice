(ns test-proj.components.atoms.input.core 
 (:require
  [re-frame.core :as rf]
  [re-com.core :as re-com :refer [at]]))
  


(defn input-text [props]
  (let [{:keys [value on-change]}  props]
    [re-com/input-text
     :src   (at)
     :model value
     :class "input"
     :change-on-blur? false
     :on-change on-change]))

(defn input-textarea [props]
  (let [{:keys [value on-change]}  props]
    [re-com/input-textarea
     :src (at)
     :model value
     :on-change on-change
     :change-on-blur? false]))



(defn input [props]
  (let [{:keys [variant]} props]
    [:<>
     (case variant
       "text" (input-text props)
       "textarea" (input-textarea props))]))