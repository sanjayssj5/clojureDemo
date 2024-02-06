(ns login-sample.routes.home
  (:require [compojure.core :refer :all]
            [login-sample.views.layout :as layout]
            [clojure.edn :as edn]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [hiccup.page :refer [html5 include-js]]))


(defn login [& [msg]]
  (html5
   [:div#logincontainer [:h2 "Login to continue!"]

    [:form#login {:hx-post "/" :hx-swap "innerHTML" :hx-target "#maincontainer"}
     [:p "Username"]
     [:input#uname.field {:type "text" :name "uname"}]
     [:p "Password"]
     [:input#password.field {:type "password" :name "pass"}]
     [:br]
     [:p#target-notify msg]
     [:br]
     [:input#loginbtn {:type "submit" :value "Login"}]]]))



(defn home 
  []
  (layout/common 
 [:div#maincontainer 
  
  [:h1 "Welcome!!"]
  (login)]
   (include-js "/js/loginValidate.js")
  
   ))





(defn validate
  [uname pass]
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) uname))
       first
       ((fn [data] (if (nil? data)
                       (login "User doesnt exist!!!")
                       (if (= (:pass data) pass)
                         ;;(str "Welcome  " (:name data))
                         (do (session/put! :user uname) 
                             (redirect "/screen1"))
                         (login "Incorrect Password")))))
       ))
(defroutes home-routes
  (GET "/" [] (home ))
  (POST "/" [uname pass] (validate uname pass))
)