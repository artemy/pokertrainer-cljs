(ns com.github.artemy.pokertrainer.hand-detectors-test
  (:require [cljs.test :refer [deftest is run-tests]]
            [com.github.artemy.pokertrainer.hand-detectors :refer [compute-correct-answer]]))

(deftest royal-flush-test
  (let [cards [{:rank 14 :suit "♥️"}
               {:rank 13 :suit "♥️"}
               {:rank 12 :suit "♥️"}
               {:rank 11 :suit "♥️"}
               {:rank 10 :suit "♥️"}]
        expected :royalFlush]
    (is (= expected (compute-correct-answer cards)))))

(deftest straight-flush-test
  (let [cards [{:rank 9 :suit "♠️"}
               {:rank 8 :suit "♠️"}
               {:rank 7 :suit "♠️"}
               {:rank 6 :suit "♠️"}
               {:rank 5 :suit "♠️"}]
        expected :straightFlush]
    (is (= expected (compute-correct-answer cards)))))

(deftest four-of-a-kind-test
  (let [cards [{:rank 14 :suit "♦️"}
               {:rank 14 :suit "♣️"}
               {:rank 14 :suit "♠️"}
               {:rank 14 :suit "♥️"}
               {:rank 2 :suit "♥️"}]
        expected :fourOfAKind]
    (is (= expected (compute-correct-answer cards)))))

(deftest full-house-test
  (let [cards [{:rank 14 :suit "♦️"}
               {:rank 14 :suit "♣️"}
               {:rank 14 :suit "♠️"}
               {:rank 13 :suit "♥️"}
               {:rank 13 :suit "♣️"}]
        expected :fullHouse]
    (is (= expected (compute-correct-answer cards)))))

(deftest full-house-edge-case-test
  "Should not detect pairs in three cards"
  (let [cards [{:rank 14 :suit "♣️"}
               {:rank 14 :suit "♥️"}
               {:rank 14 :suit "♠️"}
               {:rank 10 :suit "♦️"}
               {:rank 9 :suit "♣️"}]
        expected :fullHouse]
    (is (not= expected (compute-correct-answer cards)))))

(deftest flush-test
  (let [cards [{:rank 2 :suit "♥️"}
               {:rank 4 :suit "♥️"}
               {:rank 6 :suit "♥️"}
               {:rank 8 :suit "♥️"}
               {:rank 13 :suit "♥️"}]
        expected :flush]
    (is (= expected (compute-correct-answer cards)))))

(deftest flush-edge-case
  (let [cards [{:rank 12 :suit "♣️"}
               {:rank 4 :suit "♣️"}
               {:rank 5 :suit "♦️"}
               {:rank 2 :suit "♣️"}
               {:rank 7 :suit "♥️"}
               {:rank 14 :suit "♠️"}
               {:rank 6 :suit "♣️"}]
        expected :flush]
    (is (not= expected (compute-correct-answer cards)))))

(deftest straight-test
  (let [cards [{:rank 5 :suit "♥️"}
               {:rank 6 :suit "♣️"}
               {:rank 7 :suit "♦️"}
               {:rank 8 :suit "♠️"}
               {:rank 9 :suit "♥️"}]
        expected :straight]
    (is (= expected (compute-correct-answer cards)))))

(deftest straight-test-starting-with-ace
  (let [cards [{:rank 14 :suit "♥️"}
               {:rank 2 :suit "♣️"}
               {:rank 3 :suit "♦️"}
               {:rank 4 :suit "♠️"}
               {:rank 5 :suit "♥️"}]
        expected :straight]
    (is (= expected (compute-correct-answer cards)))))

(deftest straight-test-edge-case
  (let [cards [{:rank 14, :suit "♠️"}
                {:rank 8 :suit "♣️"}
                {:rank 13 :suit "♠️"}
                {:rank 3 :suit "♠️"}
                {:rank 2 :suit "♥️"}
                {:rank 2 :suit "♣️"}
                {:rank 12 :suit "♦️"}]
        expected :straight]
    (is (not= expected (compute-correct-answer cards)))))

(deftest three-of-a-kind-test
  (let [cards [{:rank 14 :suit "♣️"}
               {:rank 14 :suit "♥️"}
               {:rank 14 :suit "♠️"}
               {:rank 2 :suit "♦️"}
               {:rank 7 :suit "♣️"}]
        expected :threeOfAKind]
    (is (= expected (compute-correct-answer cards)))))

(deftest two-pairs-test
  (let [cards [{:rank 13 :suit "♦️"}
               {:rank 13 :suit "♣️"}
               {:rank 12 :suit "♥️"}
               {:rank 12 :suit "♠️"}
               {:rank 11 :suit "♦️"}]
        expected :twoPairs]
    (is (= expected (compute-correct-answer cards)))))

(deftest three-pairs-test
  "Three pairs combination does not exist, it's still two pairs"
  (let [cards [{:rank 13 :suit "♦️"}
               {:rank 13 :suit "♣️"}
               {:rank 12 :suit "♥️"}
               {:rank 12 :suit "♠️"}
               {:rank 11 :suit "♦️"}
               {:rank 11 :suit "♥️"}]
        expected :twoPairs]
    (is (= expected (compute-correct-answer cards)))))

(deftest pair-test
  (let [cards [{:rank 14 :suit "♣️"}
               {:rank 14 :suit "♥️"}
               {:rank 9 :suit "♠️"}
               {:rank 8 :suit "♦️"}
               {:rank 7 :suit "♣️"}]
        expected :pair]
    (is (= expected (compute-correct-answer cards)))))

(deftest high-card-test
  (let [cards [{:rank 14 :suit "♥️"}
               {:rank 8 :suit "♠️"}
               {:rank 6 :suit "♦️"}
               {:rank 4 :suit "♣️"}
               {:rank 2 :suit "♥️"}]
        expected :highCard]
    (is (= expected (compute-correct-answer cards)))))
