(ns test-proj.components.atoms.button.circle-button
  (:require
   [re-frame.core :as rf]
   [re-com.core :as re-com :refer [at]]
))

(defn circle-button [props]
  (let [{:keys [on-click icon size class]} props]
    [re-com/md-circle-icon-button
     :src (at)
     :md-icon-name icon
     :size size
     :emphasise? true
     :on-click on-click
     :class class]))