(ns test-proj.components.moleculs.forms.create-todo.core
 (:require
   [re-frame.core :as rf]
   [re-com.core :as re-com :refer [at]]
   [test-proj.subs :as subs]
   [test-proj.events :as events]
   [nano-id.core :refer [nano-id]]
   [test-proj.components.atoms.button.core :refer [button]]
   [test-proj.components.atoms.input.core :refer [input]]))

(defn new-todo-generator []
  (let [title (rf/subscribe [:create-form-title])
        text (rf/subscribe [:create-form-text])
        priority (rf/subscribe [:create-form-priority])]

    {:title @title :text @text :priority @priority :id (nano-id) :done? false}))


(defn on-create-form-submit [e]
 (let [title (rf/subscribe [:create-form-title])
        text (rf/subscribe [:create-form-text])]
       
   (.preventDefault e)
   (if (and (not-empty? @title) (not-empty? @text))
     (rf/dispatch [::events/add-new-todo (new-todo-generator)]))))


(defn create-todo-form []
  (let [title (rf/subscribe [:create-form-title])
        text (rf/subscribe [:create-form-text])
        priority (rf/subscribe [:create-form-priority])]
    
     [:form {:on-submit on-create-form-submit :class "create-form"}
      [:h3 "Create todo"]
      (button {:on-click close-modal :icon "zmdi-close" :size :smaller :class "close-modal-btn" :variant "circle"})

      (input {:variant "text" :value @title :on-change #(rf/dispatch [:new-todo-title %])})

      (input {:variant "textarea" :value @text :on-change #(rf/dispatch [::events/new-todo-text %])})

      (button {:type "submit" :variant "simple"})]))
