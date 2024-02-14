(ns login-sample.routes.screen1
  (:require [compojure.core :refer :all]
            [login-sample.views.layout :as layout]
            [clojure.edn :as edn]
            [noir.response :refer [redirect]]
            [noir.session :as session]))



(defn user-data 
  [[label data]]
  (list [:p.plabel (clojure.string/upper-case (name label))]
  [:p.pfield (str data)]
  [:br]))


(defn show-data 
  []
  (->> (slurp "auth1.edn")
       edn/read-string
       (filter #(= (:uname %) (session/get :user)))
       first
       :data
       (map user-data)))

(defn screen1 
  []
  (if (nil? (session/get :user))
    (redirect "/")
    (layout/common 
     [:h1 "Welcome   "  (session/get :user)]
     [:p  (show-data)]
     [:form#edit-btn {:action "/edit2" :method "GET"}
      [:input {:type "submit" :value "EDIT"}]]
     [:form#logout {:action "/logout" :method "POST"}
      [:input {:type "submit" :value "LOGOUT"}]])))



(defroutes screen1-routes

  (GET "/screen1" [] (screen1))
  (POST "/logout" [] (session/clear!) (redirect "/")))