(ns test-proj.views
  (:require
   [re-frame.core :as rf]
   [re-com.core :as re-com :refer [at]]
   [test-proj.subs :as subs]
   [test-proj.events :as events]
   [nano-id.core :refer [nano-id]]
   [test-proj.components.atoms.button.core :refer [button]]
   [test-proj.components.atoms.input.core :refer [input]]))
  ;;  [test-proj.components.moleculs.forms.create-todo.core :refer [create-todo-form]]
   
   

(defn done? [todo] (:done? todo)) ;; fn return done todo - true

(def not-done? (complement done?)) ;; fn return not done todo - true

(def not-empty? (complement empty?)) ;; return not empty - true

(defn close-modal [] (rf/dispatch [::events/close-modal]))

;; TITLE -------------------------------

(defn title []
  (let [name (rf/subscribe [:name])]
    [re-com/title
     :src   (at)
     :label (str "Hello " (if (not-empty? @name) @name "Stranger"))
     :level :level1]))


;; LISTS -------------------------------

(defn list-item [todo]
  [:li {:key (:id todo) :class (if (:done? todo) "task-done")}
   [:h2 (:title todo)]
   [:p (:text todo)]
   [:p (if (:done? todo) "Done" "In Process")]])

(defn not-done-todo-list []
  (let [todos (rf/subscribe [:todos])]
   (js/console.log "TODOS" (string? @todos))

   [:ul (for [todo (filter not-done? @todos)]
          (list-item todo))]))

(defn done-todo-list []
  (let [todos (rf/subscribe [:todos])]

    [:ul (for [todo (filter done? @todos)]
           (list-item todo))]))

;; NAME FORM ---------------------------


(defn on-hello-form-submit [e]
  (let [name (rf/subscribe [:name])]
    (.preventDefault e)
    (if (not-empty? @name) (close-modal))))

(defn hello-form []
  (let [name (rf/subscribe [:name])]

    [:form {:on-submit on-hello-form-submit :class "hello-form"}
     (input {:variant "text" :value @name :on-change #(rf/dispatch [::events/change-name %])})
     (button {:type "submit" :variant "simple"})]))

;; CREATE-TODO FORM ---------------------------
;; 

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

;; MODAL -------------------------------

(defn modal-name? [name]
  (let [modal-name (rf/subscribe [:modal-name])]
    (= @modal-name name)))


(defn hello-modal []
  [:div
   (title)
   [:p "Type your name:"]
   (hello-form)])

(defn create-task []
  [:div])


(defn modal-box []
  [re-com/v-box
   :src (at)
   :children [(if (modal-name? "hello-modal") [hello-modal])
              (if (modal-name? "create-todo-modal") [create-todo-form])]])


(defn modal []
  [re-com/modal-panel
   :src   (at)
   :child [modal-box]])


;; MAIN -------------------------------

(defn on-create-task-btn-click []
  (rf/dispatch [::events/open-modal])
  (rf/dispatch [::events/change-modal-name "create-todo-modal"]))

(defn main-panel []
  (let [name (rf/subscribe [:name])
        open-modal? (rf/subscribe [:open-modal?])
        todos (rf/subscribe [:todos])]
    [re-com/v-box
     :src      (at)
     :height   "100%"
     :children [(if (not-empty? @name) [title])
                (if (empty? @todos) "Create your first todo" [not-done-todo-list])
                [done-todo-list]
                [button {:on-click on-create-task-btn-click :icon "zmdi-plus" :size :larger :class "add-btn" :variant "circle"}]
                (if @open-modal? [modal])]]))


