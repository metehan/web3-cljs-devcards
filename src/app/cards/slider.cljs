(ns app.cards.slider
  (:require
   [cljsjs.react]
   [cljsjs.react.dom]
   [reagent.core :as r :include-macros true]
   [devcards.core :as devcards :include-macros true :refer [defcard]]))


(def state-data (r/atom {:start 30 :finish 0 :min -520 :max 0}))

(defn slider [state-data param value style]
  [:input {:type "range"
           :value value
           :min (:min @state-data)
           :max (:max @state-data)
           :style (merge {:width "100%"} style)
           :on-change (fn [e]
                        (let [val (int (.. e -target -value))]
                          (cond (= param :start) (when (< val (:finish @state-data)) (swap! state-data assoc param val))
                                (= param :finish) (when (> val (:start @state-data)) (swap! state-data assoc param val)))))}])

(defn slider-component [state-data]
  (let [{:keys [finish start]} @state-data]
    [:div
     [:h3 "Date range"]
     [:div {:style {:position :relative}}
      "Range: " (int finish) "days"
      [:div.slider-wrap
       [slider state-data :start start]
       [slider state-data :finish finish]]]]))

(defcard slider-card
  (devcards/reagent slider-component)
  state-data
  {:inspect-data true
   :frame true
   :history true})