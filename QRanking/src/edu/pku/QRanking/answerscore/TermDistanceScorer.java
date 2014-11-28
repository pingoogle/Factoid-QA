package edu.pku.QRanking.answerscore;

import java.util.ArrayList;
import java.util.List;

import edu.pku.QRanking.Answer;
import edu.pku.QRanking.Question;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * Answer Extraction
 *
 * @author 李琦
 * @email stormier@126.com
 * 
 */

public class TermDistanceScorer implements AnswerScorer {

	float weight;

	@Override
	public void score(Question question) {
		for (Answer answer : question.answers) {
			List<Integer> answer_positions = new ArrayList<>();
			List<Integer> question_positions = new ArrayList<>();

			int i = 1;
			for (String term : answer.evidence.evidence_content) {
				if (term.equals(answer.answer_content)) {
					answer_positions.add(i);
				}
				i++;
			}

			// interrogative is not involed in calculation
			List<String> interrogative = new ArrayList<>();
			interrogative.add("DT"); // 什么 哪
			interrogative.add("PN"); // 哪里 谁
			interrogative.add("AD"); // 多少 为什么

			for (TaggedWord term : question.tagged_title) {
				if (interrogative.contains(term.tag())) {
					continue;
				}
				i = 1;
				for (String term2 : answer.evidence.evidence_content) {
					if (term2.equals(term.word())) {
						question_positions.add(i);
					}
					i++;
				}
			}

			int distance = 0;
			for (int question_position : question_positions) {
				for (int answer_position : answer_positions) {
					distance += Math.abs(question_position - answer_position);
				}
			}

			//float score = answer.score / distance;
			//float score = weight * 50/ distance;
			float score = weight * question.title.size()/ (float)distance;
			System.out.println("term distance score:" + answer.answer_content
					+ " " + score + " distance:" + distance);
			answer.score += score;

		}

	}

}