(ns test-proj.subs
  (:require
   [re-frame.core :as re-frame]))

(def subscriptions {:create-form-title [:forms :create-todo :title]
                    :create-form-text [:forms :create-todo :text]
                    :create-form-priority [:forms :create-todo :priority]
                    :name [:name]
                    :todos [:todos]
                    :open-modal? [:modal :open?]
                    :modal-name [:modal :name]})

(defn make-sub [key]
  (re-frame/reg-sub key (fn [db] (get-in db (key subscriptions)))))

(->> (keys subscriptions)
     (map make-sub)
     (doall))
