(ns com.github.artemy.pokertrainer.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [react-bootstrap :refer [Button Col Container Modal Modal.Body Modal.Footer Modal.Header Modal.Title Row]]
            [com.github.artemy.pokertrainer.game :refer [hands pick-random-cards poker-deck rank-to-name]]
            [com.github.artemy.pokertrainer.hand-detectors :refer [compute-correct-answer]]))

;-- State
(def cards (r/atom (pick-random-cards)))
(def points (r/atom 0))
(def turns (r/atom 0))
(def status (r/atom "idle"))
(def moves (r/atom []))

(defn reset-game []
  (reset! cards (pick-random-cards))
  (reset! points 0)
  (reset! turns 0)
  (reset! status "idle")
  (reset! moves []))


(defn submit-answer [answer]
  (print @status)
  (if (not= @status "game-over")
    (let [correct-answer (compute-correct-answer @cards)]
      (swap! turns inc)
      (if (>= @turns 10)
        (reset! status "game-over"))
      (if (= correct-answer answer)
        (swap! points inc))
      (swap! moves #(conj % {:cards @cards :correct-answer correct-answer :answer answer}))
      (reset! cards (pick-random-cards)))))

;-- Components
(defn game-info-component []
  [:<>
   [:> Col {:md 3}
    [:span {:class ["align-middle" "h5"]} "Points: " @points " Turns left: " (- 10 @turns)]]
   [:> Col {:md 2}
    [:> Button
     {:variant "secondary" :size "sm" :on-click reset-game}
     "Restart game"]]])

(defn card-component [card]
  [:div {:class ["card" "m-2"]}
   [:div {:class "rank"} (-> card :rank rank-to-name)]
   [:div {:class "suit"} (:suit card)]])

(defn cards-component [cards]
  [:div {:class ["cards" "d-flex" "justify-content-center" "align-items-center"]}
   (for [card cards]
     ^{:key (str (:rank card) (:suit card))} [card-component card])])

(defn answer-buttons-component []
  [:div {:class ["answer-buttons" "d-flex" "flex-wrap" "justify-content-center" "align-items-center" "gap-1"]}
   (doall
     (for [[k v] hands]
       ^{:key k}
       [:> Button {:variant  "secondary"
                   :size     "sm"
                   :title    (:description v)
                   :disabled (= @status "game-over")
                   :on-click #(submit-answer k)} (:name v)]))])

(defn modal-component []
  [:> Modal {:show (= "game-over" @status) :size "lg" :onHide reset-game}
   [:> Modal.Header {:closeButton "true"}
    [:> Modal.Title "Game over"]]
   [:> Modal.Body
    [:> Container
     [:> Row {:class ["justify-content-center" "align-items-center"]}
      [:> Col {:md 3} "Your score: " @points]
      [:> Col {:md 3} "Correct answers: " (count (filter #(= (:correct-answer %) (:answer %)) @moves)) "/" (count @moves)]]
     (for [move @moves]
       ^{:key (:cards move)}
       [:> Row {:class "align-items-center"}
        [:> Col {:md 10}
         [cards-component (:cards move)]]
        [:> Col {:md 2}
         [:span {:class (if (= (:correct-answer move) (:answer move)) "text-success" "text-danger")}
          (:name ((:correct-answer move) hands))]
         ]])]]
   [:> Modal.Footer
    [:> Button {:variant "primary" :onClick reset-game} "Restart game"]]])

(defn app []
  [:<>
   [:> Container
    [:> Row {:class ["justify-content-center" "game-info" "m-1"]}
     (game-info-component)]
    [:> Row {:class "m-1"}
     [:> Col (cards-component @cards)]]
    [:> Row {:class ["justify-content-center" "m-1"]}
     [:> Col {:md "6"} (answer-buttons-component)]]]
   (modal-component)
   ])

(defn ^:export ^:dev/after-load run []
  (rdom/render [app] (js/document.getElementById "app")))
