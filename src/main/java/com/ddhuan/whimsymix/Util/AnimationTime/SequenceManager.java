package com.ddhuan.whimsymix.Util.AnimationTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

//管理并行的时间序列们
public final class SequenceManager {
    private final ArrayList<AnimationTimeSequence> list;
    private final int hash;

    //便于创建热重载可更新的时间序列，而不频繁new对象
    public static SequenceManager getOrCreate(SequenceManager sm, AnimationTimeSequence... args) {
        if (sm != null && Objects.hash((Object[]) args) == sm.hash)
            return sm;
        else return new SequenceManager(args);
    }

    public static SequenceManager getOrCreate(SequenceManager sm, Integer[]... args) {
        if (sm != null && sm.list.size() == args.length) {
            for (int i = 0; i < args.length; i++)
                if (Objects.hash((Object[]) args[i]) != sm.list.get(i).getHash())
                    return new SequenceManager(Arrays.stream(args).map(AnimationTimeSequence::new).toArray(AnimationTimeSequence[]::new));
            return sm;
        }
        return new SequenceManager(Arrays.stream(args).map(AnimationTimeSequence::new).toArray(AnimationTimeSequence[]::new));
    }

    public SequenceManager(AnimationTimeSequence... sequence) {
        this.list = new ArrayList<>();
        this.list.addAll(Arrays.asList(sequence));
        this.hash = this.hashCode();
    }


    public AnimationTimeSequence byIndex(int index) {
        if (index < 0) return list.get(0);
        else if (index >= list.size()) return list.get(list.size() - 1);
        else return list.get(index);
    }

    public int getHash() {
        return hash;
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SequenceManager && ((SequenceManager) obj).hash == this.hash
                && ((SequenceManager) obj).list.equals(this.list);
    }
}
