(ns login-sample.routes.edit2
  (:require [compojure.core :refer :all]
            [login-sample.views.layout :as layout]
            [clojure.edn :as edn]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [hiccup.page :refer [include-js]]))


(defn user-data [[label data]]
  (list [:form {:action (str "/change" (name label))  :method "POST" :id (str (name label) "form")}
         [:p  {:class "plabel"} (clojure.string/upper-case (name label))]
         [:p  {:class "pfield" :id (str (name label) "txt")} (str data)]
         [:input.field {:type "hidden" :name "uname":value (session/get :user) :id (str (name label) "txtkey")}]
         [:input.field {:type "hidden" :name label :value data :id (str (name label) "txtbox")}]
         [:input {:type "hidden" :value "SUBMIT"  :id (str (name label) "sub")}]]

        [:button {:id (str (name label) "btn") } "CHANGE"]
        [:br]))


(defn show-data []
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) (session/get :user)))
       first
       :data
       (map user-data)))

(defn edit2 [& [msg]]
  (if (nil? (session/get :user))
    (redirect "/")
    (layout/common
     [:h1 "Welcome   "  (session/get :user)]
     [:p  (show-data)]
     [:p msg]

     [:form#logout {:action "/logout" :method "POST"}
      [:input {:type "submit" :value "LOGOUT"}]]
     (include-js "/js/edit.js")

     )))


(defn validate
  [uname]
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) uname))
       first))

(defn change-data
  [uname label data]

  (->> (slurp "auth1.edn")
       edn/read-string
       (map (fn [record]
              (if (= (:uname record) uname)
                (assoc record :data (assoc (:data record ) label data))
                record)))
       (filter identity)
       (#(pprint/pprint % (io/writer "auth1.edn"))))
  (redirect "/edit2"))


(defn changename
  [uname name]
  (cond
    (nil? (validate uname)) (edit2 "User credentials Invalid!!")
    (empty? uname) (edit2 "Username is empty.")
    (empty? name) (edit2  "Name is empty.")
    (> (count uname) 30) (edit2 "Invalid User Name. Max characters(30)")
    (> (count name) 40) (edit2 "Invalid  Name. Max characters(40)")
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (edit2 "Invalid User Name")
    (nil? (re-seq #"[a-zA-Z]{3,40}" name)) (edit2 "Invalid Name.")
    :else (change-data uname (keyword "name") name )))

(defn changeage
  [uname age]
  (cond
    (nil? (validate uname)) (edit2 "User credentials Invalid!!")
    (empty? uname) (edit2 "Username is empty.")
    (empty? age) (edit2 "Age is empty. Enter a value!")
    (> (count uname) 30) (edit2 "Invalid User Name. Max characters(30)")
    (> (count age) 2) (edit2 "Invalid Age.")
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (edit2 "Invalid User Name")
    (nil? (re-seq #"[0-9]{2}" age)) (edit2 "Invalid Age") 
    (or (< (Integer/parseInt age) 6) (> (Integer/parseInt age) 99)) (edit2 "Age should be in between limits 6 and 99")
    :else (change-data uname (keyword "age") age)))


(defn changephone
  [uname phone]
  (cond
    (nil? (validate uname)) (edit2 "User credentials Invalid!!")
    (empty? uname) (edit2 "Username is empty.") 
    (empty? phone) (edit2 "Phone number is empty. Enter a value!")
    (> (count uname) 30) (edit2 "Invalid User Name. Max characters(30)") 
    (> (count phone) 10) (edit2 "Invalid Phone. Max characters(10)")
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (edit2 "Invalid User Name")
    (nil? (re-seq #"[1-9]{1}[0-9]{9}" phone)) (edit2 "Invalid Phone Number")
    :else (change-data uname (keyword "phone") phone)))


(defn changeaddress
  [uname address]
  (cond
    (nil? (validate uname)) (edit2 "User credentials Invalid!!")
    (empty? uname) (edit2 "Username is empty.")
    (> (count uname) 30) (edit2 "Invalid User Name. Max characters(30)")
    (> (count address) 70) (edit2 "Invalid  Address. Max characters(70)")
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (edit2 "Invalid User Name")
    :else (change-data uname (keyword "address") address)))






(defroutes edit-routes2

  (GET "/edit2" [] (edit2))
  (POST "/changename" [uname name] (changename uname name))
  (POST "/changeage" [uname age] (changeage uname age))
  (POST "/changephone" [uname phone] (changephone uname phone))
  (POST "/changeaddress" [uname address] (changeaddress uname address))
  )