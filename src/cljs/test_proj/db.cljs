(ns test-proj.db)

(def default-db
  {:name ""
   
   :modal {:open? false
           :name "hello-modal"}
   
   :forms {:create-todo {:title ""
                         :text ""
                         :priority "normal"}}
       
   :todos []})
  
