(ns com.github.artemy.pokertrainer.game-test
  (:require [cljs.test :refer [deftest is]]
            [com.github.artemy.pokertrainer.game :refer [poker-deck rank-to-name]]))

(deftest cards-creation
  (is (= 52 (count poker-deck))))

(deftest rank-to-name-test
  (let [input [2 5 7 11 12 13 14]
        expected ["2" "5" "7" "J" "Q" "K" "A"]]
    (doall
      (map (fn [i e] (is (= e (rank-to-name i))))
           input
           expected))))
