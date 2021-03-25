(ns test-proj.components.atoms.button.core
  (:require
   [re-frame.core :as rf]
   [re-com.core :as re-com :refer [at]]
   ))

 
(defn simple-button [props]
  (let [{:keys [type on-click]} props]
    [re-com/button
     :src   (at)
     :label "done"
     :class "basic-btn"
     :on-click on-click
     :attr {:type type}]))


(defn circle-button [props]
  (let [{:keys [on-click icon size class]} props]
    [re-com/md-circle-icon-button
     :src (at)
     :md-icon-name icon
     :size size
     :emphasise? true
     :on-click on-click
     :class class]))


(defn button [props]
  (let [{:keys [variant]} props]
    [:<>
     (case variant
       "simple" (simple-button props)
       "circle" (circle-button props))]))