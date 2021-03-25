(ns test-proj.components.atoms.button.simple-button
 (:require
  [re-frame.core :as rf]
  [re-com.core :as re-com :refer [at]]))
 
(defn simple-button [props]
  (let [{:keys [type on-click]} props]
    [re-com/button
     :src   (at)
     :label "done"
     :class "basic-btn"
     :on-click on-click
     :attr {:type type}]))

