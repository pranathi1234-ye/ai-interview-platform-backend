package com.pranathi.ai_interview_backend.dto;

public class FeedbackResult {

    private String question;
    private String answer;
    private int score;
    private String feedback;
    private String suggestion;

    public FeedbackResult() {
    }

    public FeedbackResult(
            String question,
            String answer,
            int score,
            String feedback,
            String suggestion) {

        this.question = question;
        this.answer = answer;
        this.score = score;
        this.feedback = feedback;
        this.suggestion = suggestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}