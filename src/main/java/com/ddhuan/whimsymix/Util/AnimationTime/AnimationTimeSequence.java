package com.ddhuan.whimsymix.Util.AnimationTime;

import java.util.ArrayList;
import java.util.Objects;

//用于动画的时间序列
public final class AnimationTimeSequence {
    private final ArrayList<Integer> sequenceList;
    private final ArrayList<Integer> previousSequenceList;
    private final int hash;

    //便于创建热重载可更新的时间序列，而不频繁new对象
    public static AnimationTimeSequence getOrCreate(AnimationTimeSequence sequence, Integer... args) {
        if (sequence != null && Objects.hash((Object[]) args) == sequence.hash)
            return sequence;
        else return new AnimationTimeSequence(args);
    }

    public AnimationTimeSequence(Integer... args) {
        this.sequenceList = new ArrayList<>(args.length);
        this.previousSequenceList = new ArrayList<>(args.length);
        int sum = 0;
        for (Integer i : args) {
            sequenceList.add(i);
            previousSequenceList.add(sum);
            sum += i;
        }
        this.hash = this.hashCode();
    }


    public int getHash() {
        return hash;
    }

    public int getSeq(final int ordinal) {
        if (ordinal <= 0 || ordinal > sequenceList.size())
            return 0;
        else
            return sequenceList.get(ordinal - 1);
    }

    public int getPreSeq(final int ordinal) {
        if (ordinal <= 0) return 0;
        else if (ordinal > previousSequenceList.size())
            return previousSequenceList.get(previousSequenceList.size() - 1) + getSeq(previousSequenceList.size());
        else return previousSequenceList.get(ordinal - 1);
    }

    //线性增值
    public double getLinearAdd(int ordinal, int animationTime, double maxValue) {
        return Math.min(1, Math.max(0, (double) (animationTime - this.getPreSeq(ordinal))) / this.getSeq(ordinal)) * maxValue;
    }

    //指数增值
    public double getExponentAdd(int ordinal, int animationTime, double power, double maxValue) {
        double t = Math.min(1, Math.max(0, (double) (animationTime - this.getPreSeq(ordinal))) / this.getSeq(ordinal));
        return Math.pow(t, 3.0) * maxValue;
    }

    //反指数增值
    public double getReExponentAdd(int ordinal, int animationTime, double power, double maxValue) {
        double t = Math.min(1, Math.max(0, (double) (animationTime - this.getPreSeq(ordinal))) / this.getSeq(ordinal));
        return (1 - Math.pow((1 - t), power)) * maxValue;
    }

    @Override
    public int hashCode() {
        return sequenceList.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnimationTimeSequence && ((AnimationTimeSequence) obj).hash == this.hash
                && ((AnimationTimeSequence) obj).sequenceList.equals(this.sequenceList);
    }
}
