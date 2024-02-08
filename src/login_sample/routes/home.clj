(ns login-sample.routes.home
  (:require [compojure.core :refer :all]
            [login-sample.views.layout :as layout]
            [clojure.edn :as edn]
            [noir.response :refer [redirect]]
            [noir.session :as session]))

(defn home [& [msg]]
  (layout/common 
   [:h1 "Welcome!!"]
   [:div#logincontainer [:h2 "Login to continue!"]
   [:form#login {:action "/" :method "POST"}
    [:p "Username"]
    [:input#uname.field {:type "text" :name "uname" :required ""}] 
    [:p "Password"]
    [:input#password.field {:type "password" :name "pass" :required ""} ]
    [:br]
    [:p msg]
    [:br]
    [:input#loginbtn {:type "submit" :value "Login" }]]]
   ))






(defn validate
  [uname pass]
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) uname))
       first
       ((fn [data] (if (nil? data)
                       (home "User doesnt exist!!!")
                       (if (= (:pass data) pass)
                         ;;(str "Welcome  " (:name data))
                         (do (session/put! :user uname) 
                             (redirect "/screen1"))
                         (home "Incorrect Password")))))
       ))
(defroutes home-routes
  (GET "/" [] (home ))
  (POST "/" [uname pass] (validate uname pass))
)