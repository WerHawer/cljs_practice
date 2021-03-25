(ns test-proj.events
  (:require
   [re-frame.core :as re-frame]
   [test-proj.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [com.degel.re-frame.storage]
   [cljs.reader]
))

(def not-empty? (complement empty?))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-fx
 :persist-from-ls

 [(re-frame/inject-cofx :storage/get {:names [:name :todos]})]

 (fn [{db :db {:keys [name todos]} :storage/get}]
   {:fx [(if (empty? name) [:dispatch [::open-modal]])
         (if (not-empty? name) [:dispatch [::change-name name]])
         [:dispatch [:persist-todos (cljs.reader/read-string todos)]]
         ]}))
         
(re-frame/reg-event-fx
 ::change-name
 (fn [cofx [_ new-name]]
   {:storage/set {:session? false
                  :name :name :value new-name}
    :db (assoc (:db cofx) :name new-name)}))

(re-frame/reg-event-db
 ::close-modal
 (fn [db _]
   (assoc-in db [:modal :open?] false)))

(re-frame/reg-event-db
 ::open-modal
 (fn [db _]
   (assoc-in db [:modal :open?] true)))

(re-frame/reg-event-db
 ::change-modal-name
 (fn [db [_ new-name]]
   (assoc-in db [:modal :name] new-name)))

(re-frame/reg-event-fx
 ::add-new-todo
 (fn [cofx [_ new-todo]]
   {:db  (update-in (:db cofx) [:todos] conj new-todo)
    :storage/set {:session? false
                  :name :todos :value (conj (get (:db cofx) :todos) new-todo)}
    :fx [[:dispatch [::close-modal]]
         [:dispatch [::refresh-create-todo-form]]]}))

(re-frame/reg-event-db
 :persist-todos
 
 (fn [db [_ p-todos]]
  (assoc-in db [:todos]  p-todos)))

(re-frame/reg-event-db
 :new-todo-title
 (fn [db [_ new-title]]
   (assoc-in db [:forms :create-todo :title] new-title)))

(re-frame/reg-event-db
 ::new-todo-text
 (fn [db [_ new-text]]
   (assoc-in db [:forms :create-todo :text] new-text)))

(re-frame/reg-event-db
 ::refresh-create-todo-form
 (fn [db _]
   (assoc-in db [:forms :create-todo] {:title ""
                                       :text ""
                                       :priority "normal"})))
