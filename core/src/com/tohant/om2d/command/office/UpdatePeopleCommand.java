package com.tohant.om2d.command.office;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.UiActorService;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;

public class UpdatePeopleCommand implements Command {

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        Array<Actor> staff = uiActorService.getActorsByIdPrefix(STAFF.name());
        Array<Actor> cells = uiActorService.getActorsByIdPrefix(CELL.name());
        AtomicReference<Float> moodIndex = new AtomicReference<>(1.0f);
        AtomicReference<Float> hungerIndex = new AtomicReference<>(0.0f);
        AtomicLong cleaningAmount = new AtomicLong(0L);
        AtomicLong caffeAmount = new AtomicLong(0L);
        AtomicLong securityAmount = new AtomicLong(0L);
        cells.forEach(c -> {
            if (c instanceof Cell && ((Cell) c).getRoomModel() != null) {
                Room.Type type = ((Cell) c).getRoomModel().getRoomInfo().getType();
                if (type == Room.Type.CLEANING) {
                    moodIndex.updateAndGet(f -> Float.sum(f, 0.01f));
                    cleaningAmount.incrementAndGet();
                } else if (type == Room.Type.CAFFE) {
                    moodIndex.updateAndGet(f -> Float.sum(f, 0.03f));
                    hungerIndex.updateAndGet(f -> Float.sum(f, 0.01f));
                    caffeAmount.incrementAndGet();
                } else if (type == Room.Type.SECURITY) {
                    moodIndex.updateAndGet(f -> Float.sum(f, 0.04f));
                    securityAmount.incrementAndGet();
                }
            }
        });
        moodIndex.updateAndGet(f -> Float.sum(f, hungerIndex.get()));
        staff.forEach(a -> {
            if (a instanceof Staff) {
                float subtractConstant = (staff.size - (1f / staff.size * 100.0f));
                ((Staff) a).getManInfo().setMood((((Staff) a).getManInfo().getMood()
                        + (cleaningAmount.get() <= 0 ? subtractConstant : ((float) cleaningAmount.get() / staff.size * 100.0f))
                        + (caffeAmount.get() <= 0 ? subtractConstant : ((float) caffeAmount.get() / staff.size * 100.0f))
                        + (securityAmount.get() <= 0 ? subtractConstant : ((float) securityAmount.get() / staff.size * 100.0f))
                        * moodIndex.get()));
            }
        });

        StringBuilder result = new StringBuilder("Totals:\n");
        Map<Staff.Type, String> info = new LinkedHashMap<>();
        Arrays.stream(Staff.Type.values()).forEach(s -> info.put(s, " " + s.name().charAt(0)
                + s.name().substring(1).toLowerCase() + ": 0"));
        Arrays.stream(staff.toArray(Actor.class)).collect(Collectors.groupingBy(c -> ((Staff) c).getType()))
                .forEach((k, v) -> {
                    double avgMood = v.stream().collect(Collectors.averagingDouble(s -> ((Staff) s).getManInfo().getMood()));
                    StringBuilder label = new StringBuilder();
                    label.append(" ");
                    label.append(k.name().charAt(0));
                    label.append(k.name().substring(1).toLowerCase());
                    label.append(": ");
                    label.append(v.size());
                    label.append(", avg. mood: ");
                    label.append(Math.round(avgMood));
                    label.append("%");
                    if (k != Staff.Type.WORKER) {
                        label.append(", salary: ")
                                .append(Math.round(k.getSalary()))
                                .append(" $/m.");
                    }
                    info.put(k, label.toString());
                });
        DefaultModal modal = (DefaultModal) uiActorService.getActorById(PEOPLE_INFO_MODAL.name());
        info.forEach((k, v) -> {
            result.append(v);
            result.append("\n");
        });
        modal.updateContentText(PEOPLE_INFO_LABEL.name(), result.toString());
    }

}
