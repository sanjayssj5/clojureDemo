(ns login-sample.routes.edit2
  (:require [compojure.core :refer :all]
            [login-sample.views.layout :as layout]
            [clojure.edn :as edn]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [hiccup.page :refer [html5 include-js]]))

(defn user-data 
  [[label data]]
  (list ;;[:form {:hx-post (str "/change" (name label)) :hx-include "[name= 'age']" :hx-target "body" :hx-swap "innerHTML"  :id (str (name label) "form")}]
          [:p  {:class "plabel"} (clojure.string/upper-case (name label))]
          [(keyword (str "div#" (name label))) [:p   (str data)] ]
          [:input.field (into {:type "hidden" 
                               :name label 
                               :value data 
                               :id (str (name label) "txtbox")
                               }(case (name label)
                                  "name" {:required "" 
                                          :minlength "3" 
                                          :maxlength "40"} 
                                  "age" {:required "" 
                                         :minlength "1" 
                                         :maxlength "2"}
                                  "phone" {:required "" 
                                           :minlength "10" 
                                           :maxlength "10" 
                                           :pattern "[1-9]{1}[0-9]{9}"}
                                  "address" { :maxlength "70" }
                                  nil))]
          [:button {:hx-on:click  (str  (name label) "validate(event); return false;")
                    :hx-post (str "/change" (name label)) 
                    ;;:hx-swap "none" 
                    ;;:hx-target (str "#" (name label)) 
                    :style "display:none"  
                    :id (str (name label) "sub") 
                    :hx-include (str " [name = 'uname'] , [name = '" (name label) "']") } 
           "Submit"]
          [:button {:id (str (name label) "btn")
                    :onclick (str (name label) "chng()")} 
           "Change"]
          [:br]))


(defn show-data 
  [usrname]
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) usrname))
       first
       :data
       (map user-data)))

(defn edit-body
  [& [msg]]
  (html5  [:div#edit-body 
           [:p  (show-data (session/get :user))]
           [:p#error msg]]))


(defn edit2 
  []
  (if (nil? (session/get :user))
    (redirect "/")
    (layout/common
     [:h1 "Welcome   "  (session/get :user)]
     [:input.field {:type "hidden" 
                    :name "uname" 
                    :value (session/get :user)}]
     (edit-body)


     [:form#back {:action "/screen1"
                  :method "GET"}
      [:input {:type "submit" 
               :value "Back"}]]
     [:form#logout {:action "/logout" 
                    :method "POST"}
      [:input {:type "submit" 
               :value "LOGOUT"}]]
     (include-js  "/js/edit.js"))))


(defn validate
  [uname]
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) uname (session/get :user)))
       first))

(defn change-data
  [uname label data]

  (->> (slurp "auth1.edn")
       edn/read-string
       (map (fn 
              [record]
                (if (= (:uname record) uname )
                  (assoc record :data (assoc (:data record ) label data))
                  record)))
       (filter identity)
       (#(pprint/pprint % (io/writer "auth1.edn"))))
  (html5 [(keyword (str "div#" (name label))) {:hx-swap-oob "innerHTML"} [:p data]]
         [:p#error {:hx-swap-oob "outerHTML"} ""]))


(defn changename
  [uname name]
  (cond
    (nil? (validate uname))
    (html5 [:p uname] [:p "User credentials Invalid!!"])
    (empty? uname) (html5 [:p uname] [:p "Username is empty."])
    (> (count uname) 30) (html5 [:p uname] [:p "Invalid User Name. Max characters(30)"])
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (html5 [:p uname] [:p "Invalid User Name"])
    (empty? name) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Name is empty."])
    (> (count name) 40) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Invalid  Name. Max characters(40)"])
    (nil? (re-seq #"[a-zA-Z]{3,40}" name)) (html5 [:p#error  {:hx-swap-oob "outerHTML"} "Invalid Name."])
    :else (change-data uname (keyword "name") name)))

(defn changeage
  [uname age]
  (cond
    (nil? (validate uname)) (html5 [:p uname] [:p "User credentials Invalid!!"])
    (empty? uname) (html5 [:p uname] [:p "Username is empty."])
    (> (count uname) 30) (html5 [:p uname] [:p "Invalid User Name. Max characters(30)"])
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (html5 [:p uname] [:p "Invalid User Name"])
    (empty? age) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Age is empty. Enter a value!"]) 
    (> (count age) 2) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Invalid Age."]) 
    (nil? (re-seq #"[0-9]{2}" age)) (html5 [:p#error  {:hx-swap-oob "outerHTML"} "Invalid Age"]) 
    (or (< (Integer/parseInt age) 6) (> (Integer/parseInt age) 99)) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Age should be in between limits 6 and 99"])
    :else (change-data uname (keyword "age") age)))


(defn changephone
  [uname phone]
  (cond 
    (nil? (validate uname)) (html5 [:p uname] [:p "User credentials Invalid!!"])
    (empty? uname) (html5 [:p uname] [:p "Username is empty."])
    (> (count uname) 30) (html5 [:p uname] [:p "Invalid User Name. Max characters(30)"])
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (html5 [:p uname] [:p "Invalid User Name"])
    (empty? phone) (html5 [:p#error  {:hx-swap-oob "outerHTML"} "Phone number is empty. Enter a value!"])
    (> (count phone) 10) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Invalid Phone. Max characters(10)"])
    (nil? (re-seq #"[1-9]{1}[0-9]{9}" phone)) (html5 [:p#error  {:hx-swap-oob "outerHTML"} "Invalid Phone Number"])
    :else (change-data uname (keyword "phone") phone)))


(defn changeaddress
  [uname address]
  (cond 
    (nil? (validate uname)) (html5 [:p uname] [:p "User credentials Invalid!!"])
    (empty? uname) (html5 [:p uname] [:p "Username is empty."])
    (> (count uname) 30) (html5 [:p uname] [:p "Invalid User Name. Max characters(30)"])
    (nil? (re-seq #"[a-zA-Z]{3,30}" uname)) (html5 [:p uname][:p "Invalid User Name"])
    (> (count address) 70) (html5  [:p#error  {:hx-swap-oob "outerHTML"} "Invalid  Address. Max characters(70)"])
    :else (change-data uname (keyword "address") address)))






(defroutes edit-routes2

  (GET "/edit2" [] (edit2)) 
  (POST "/changename" [uname name] (changename uname name))
  (POST "/changeage" [uname age] (changeage uname age))
  (POST "/changephone" [uname phone] (changephone uname phone))
  (POST "/changeaddress" [uname address] (changeaddress uname address)))