package com.tohant.om2d.model.man;

public class ManInfo {

    private float mood;
    private float hunger;
    private MoodStatus moodStatus;

    public ManInfo(float mood, float hunger) {
        this.mood = validateRange(mood);
        this.hunger = validateRange(hunger);
    }

    public float getMood() {
        return mood;
    }

    public void setMood(float mood) {
        this.mood = validateRange(mood);
        setMoodStatus(this.mood);
    }

    public float getHunger() {
        return hunger;
    }

    public void setHunger(float hunger) {
        this.hunger = validateRange(hunger);
    }

    private float validateRange(float value) {
        if (Float.compare(value, 0.0f) <= 0) {
            return 0.0f;
        }
        if (Float.compare(value, 100.0f) >= 0) {
            return 100.0f;
        }
        return value;
    }

    private void setMoodStatus(float value) {
        boolean isGreaterZero = Float.compare(value, 0.0f) >= 0;
        boolean isGreaterTwenty = Float.compare(value, 20.0f) >= 0;
        boolean isGreaterForty  = Float.compare(value, 40.0f) >= 0;
        boolean isGreaterSeventy = Float.compare(value, 70.0f) >= 0;
        boolean isGreaterNinety = Float.compare(value, 90.0f) >= 0;
        this.moodStatus = isGreaterNinety ? MoodStatus.HAPPY
                : isGreaterSeventy ? MoodStatus.SATISFIED
                : isGreaterForty ? MoodStatus.NORMAL
                : isGreaterTwenty ? MoodStatus.NOT_SATISFIED
                : MoodStatus.ANGRY;
    }

    public enum MoodStatus {
        ANGRY, NOT_SATISFIED, NORMAL, SATISFIED, HAPPY
    }

}
