(ns com.github.artemy.pokertrainer.game)

(def ranks (range 2 15))
(def suits ["♥️" "♠️" "♣️" "♦️"])
(def poker-deck (for [r ranks s suits] {:rank r :suit s}))

(defn pick-random-cards [] (doall (take 7 (shuffle poker-deck))))

(defn rank-to-name [rank]
  (case rank
    11 "J"
    12 "Q"
    13 "K"
    14 "A"
    (str rank)))

(def hands (array-map :highCard {:name "High Card" :description "No other combinations and only a single card"}
                      :pair {:name "Pair" :description "Two cards of the same kind"}
                      :twoPairs {:name "Two Pairs" :description "Two sets of two cards of the same kind"}
                      :threeOfAKind {:name "Three of a kind" :description "Three cards of the same kind"}
                      :straight {:name "Straight" :description "Five cards that follow each other. Ace can follow a King or start a straight followed by a Two"}
                      :flush {:name "Flush" :description "Five cards all in the same suit"}
                      :fullHouse {:name "Full House" :description "Combination of Three of a Kind with a Pair"}
                      :fourOfAKind {:name "Four of a kind" :description "Four of the same cards"}
                      :straightFlush {:name "Straight Flush" :description "Straight + Flush"}
                      :royalFlush {:name "Royal Flush" :description "Straight Flush from 10 to Ace"}))
