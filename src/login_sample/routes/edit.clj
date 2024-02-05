(ns login-sample.routes.edit 
    (:require [compojure.core :refer :all]
            [login-sample.views.layout :as layout]
            [clojure.edn :as edn]
            [noir.response :refer [redirect]]
            [noir.session :as session] 
              [clojure.java.io :as io]
              [clojure.pprint :as pprint]
              [hiccup.page :refer [include-js]])
  )


(defn user-data [[label data]]
  (list [:p.plabel (clojure.string/upper-case (name label))]
        [:input.field {:type "text" :name label :value data :id label} ]
        [:br]))

(defn show-data []
  (->> (slurp "auth.edn")
       edn/read-string
       (filter #(= (:uname %) (session/get :user)))
       first
       :data
       (map user-data)))



(defn edit [& [msg]]
  (if (nil? (session/get :user))
    (redirect "/")
    (layout/common
     [:h1 "Edit your data here"]
     [:form#changeform {:action "/change" :method "POST"}
      [:input.field {:type "hidden" :name "uname" :value (session/get :user)}]
      (show-data)
      [:br]
      [:input#changebtn {:type "submit" :value "Change"}]] 
     [:p msg]
     (include-js "/js/changeValidate.js")
     )))




(defn change-data
  [uname name age phone address]

  (->> (slurp "auth.edn")
       edn/read-string
       (map (fn [record]
              (if (= (:uname record) uname)
                (assoc record :data {:name name :age (Integer/parseInt age) :phone (Long/parseLong  phone) :address address})
                record
                )))
       (filter identity)
       (#(pprint/pprint % (io/writer "auth.edn")))) 
  (redirect "/screen1")
)


(defn validate 
  [uname ]
  (->> (slurp "auth.edn")
       edn/read-string
       (filter #(= (:uname %) uname))
       first ))

(defn change 
  [uname name age phone address ]
 (cond
   (nil? (validate uname )) (edit "User credentials Invalid!!")

   (empty? uname) (edit "Username is empty.")
   (empty? name) (edit "Name is empty. Enter a value!")
   (empty? age) (edit "Age is empty. Enter a value!")
   (empty? phone) (edit "Phone number is empty. Enter a value!")

   (> (count uname) 30) (edit "Invalid User Name. Max characters(30)")
   (> (count name) 40) (edit "Invalid  Name. Max characters(40)")
   (> (count phone) 10) (edit "Invalid Phone. Max characters(10)")
   (> (count age) 2) (edit "Invalid Age.")
   (> (count address) 70) (edit "Invalid  Address. Max characters(70)")

   (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (edit "Invalid User Name")
   (nil? (re-seq #"[a-zA-Z]{3,40}" name)) (edit "Invalid Name")
   (nil? (re-seq #"[1-9]{1}[0-9]{9}" phone)) (edit "Invalid Phone Number")
   (nil? (re-seq #"[0-9]{2}" age)) (edit "Invalid Age")
   (or (< (Integer/parseInt age) 6) (> (Integer/parseInt age) 99)) (edit "Age should be in between limits 6 and 99")

   :else (change-data uname name age phone address))
  )

(defroutes edit-routes

  (GET "/edit" [] (edit))
  (POST "/change" [uname name age phone address ] (change uname name age phone address))
  )