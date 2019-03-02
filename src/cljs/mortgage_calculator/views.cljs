(ns mortgage-calculator.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [mortgage-calculator.events :as events]
            [mortgage-calculator.utils :as u]
            [goog.dom :as gdom]
            [goog.json :as gjson]))


(defn navbar []
 [:nav.navbar
  {:aria-label "main navigation", :role "navigation"}
  [:div.navbar-brand
   [:span.navbar-item {:href "https://www.lifecheq.co.za/"}
    [:img { :width "128"
           :alt "lifeCheq"
           :src "/images/lifecheq.svg"}]]
   [:div.columns.is-vcentered
    [:span.is-size-2.has-text-centered "lifeCheq"]]
   [:a.navbar-burger
    {:aria-expanded "false", :aria-label "menu", :role "button"}
    [:span  "lifeCheq"]   {:aria-hidden "false"}
    [:span {:aria-hidden "true"}]
    [:span {:aria-hidden "true"}]]]])


(defn instructions []
   [:section.section
    [:div.container;.columns.is-centered
     [:h1.title "How to use it"]
     [:h2.subtitle
      "This is a simple  calculator for your mortgage expenses "]]])


(defn- form-values->clj [the-form]
 (let [form (js->clj (.toObject
                       (.getFormDataMap ^js gdom/forms the-form))
                     :keywordize-keys true)]
   (into {} (map (fn [[x [y]]] (vector x y)) (seq form)))))

(defn- submit-fn [event]
  (.preventDefault event)
  (let [this-form (u/oget  event 'target)]
    (rf/dispatch [::events/add-entry (form-values->clj this-form)])))


(defn list-of-mortgages [mortgage-list]
    [:div.column.is-2
     [:br]
     [:div.columns
      [:div.column.is-12.has-text-centered.is-size-4 [:span "Saved mortgages"]]
      [:div.column.is-3 [:button.button.is-primary.is-size-7 {:on-click #(rf/dispatch [::events/clear-store])} "CLEAR ALL"]]]
     (for [[[x1 _]  [ x2 _] ] (partition-all 2 (seq mortgage-list))]
      [:div.columns.is-mobile {:key (str x1 '+ x2)}
       [:div.column.is-half.has-text-centered [:a  x1]]
       [:div.column.is-half.has-text-centered [:a  x2]]])])



(defn calculator [selected-mortgage mortgage-list]
 (let [pattern "(\\d+\\.?)?\\d+"]
  [:section.section
   [:div.columns
    [:div.column.is-2]
    [:form.column.is-4.mortgage-form {:on-submit submit-fn}
     [:br]
     [:div.columns
      [:div.column.is-12.has-text-centered.is-size-4
       [:span "Mortgage Calculator"] [:span]]]
     [:div.columns.is-mobile.has-background-grey-lighter
      [:div.column.is-half.has-text-centered [:span "Loan's principal"]]
      [:div.column.is-half
       [:div.field
        [:div.control
         [:input.input.is-primary.is-medium.is-size-6
          {:name :principal-of-loan
           :pattern pattern}]]]]]
     [:div.columns.is-mobile.has-background-grey-lighter
      [:div.column.is-half.has-text-centered [:span "Deposit paid"]]
      [:div.column.is-half
       [:div.field
        [:div.control
         [:input.input.is-primary.is-medium.is-size-6
          {:name :deposit-paid :pattern pattern}]]]]]
     [:div.columns.is-mobile.has-background-grey-lighter
      [:div.column.is-half.has-text-centered
       [:span "Interest rate %"]]
      [:div.column.is-half
       [:div.field
        [:div.control
         [:div [:input.input.is-primary.is-medium.is-size-6
                 {:name :interest-rate :pattern pattern}]]]]]

      [:div.column.is-1]]
     [:div.columns.is-mobile
      [:div.column.is-3
        [:input.button.is-primary.is-size-7
         {:type "submit"
          :value "save and calculate"}]]
      [:div.column.
       [:div.field
        [:div.control
         [:input.input.is-primary.is-medium.is-size-6
          {:name :entry-name}]]]]]]
    [list-of-mortgages mortgage-list]]]))


(defn mortgage-information [[monthly-payment  a-list]]
  [:section.section
   [:div.columns
    [:div.column.is-2
     [:div.is-size-5 "Monthly payment " monthly-payment]
     [:table.table.is-bordered.is-striped.is-narrow.is-hoverable.is-fullwidth
      [:thead [:tr [:th "Percentage Paid"] [:th "Percentage Remaining"]]]
      [:tbody
       (for [[paid remaining] a-list]
         [:tr {:key (random-uuid)} [:td paid] [:td remaining]])]]]]])

(defn mortgage-bar-chart[]
   [:section.section
    [:div.container
     [:h1.title "Section"]
     [:h2.subtitle
      " A simple container to divide your page into "
      [:strong "sections"]
      ", like the one you're currently reading"]]])


