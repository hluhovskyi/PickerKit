package com.dewarder.pickerkit.panel;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dewarder.pickerkit.R;

import java.util.ArrayList;
import java.util.List;


public final class PickerCategories {

    public static List<PickerCategory> all(Context context) {
        List<PickerCategory> list = new ArrayList<>();
        list.add(camera(context));
        list.add(gallery(context));
        list.add(video(context));
        list.add(music(context));
        list.add(file(context));
        list.add(contact(context));
        list.add(location(context));
        list.add(hide(context));
        return list;
    }

    public static List<PickerCategory> media(Context context) {
        List<PickerCategory> list = new ArrayList<>();
        list.add(camera(context));
        list.add(gallery(context));
        list.add(video(context));
        list.add(music(context));
        list.add(file(context));
        list.add(hide(context));
        return list;
    }

    public static PickerCategory camera(Context context) {
        return PickerCategory.of(
                R.id.picker_category_camera,
                context.getString(R.string.label_camera),
                ContextCompat.getColor(context, R.color.attachment_panel_category_camera),
                R.drawable.ic_camera_white_24dp);
    }

    public static PickerCategory gallery(Context context) {
        return PickerCategory.of(
                R.id.picker_category_gallery,
                context.getString(R.string.label_gallery),
                ContextCompat.getColor(context, R.color.attachment_panel_category_gallery),
                R.drawable.ic_gallery_white_24dp);
    }

    public static PickerCategory video(Context context) {
        return PickerCategory.of(
                R.id.picker_category_video,
                context.getString(R.string.label_video),
                ContextCompat.getColor(context, R.color.attachment_panel_category_video),
                R.drawable.ic_video_white_24dp);
    }

    public static PickerCategory music(Context context) {
        return PickerCategory.of(
                R.id.picker_category_music,
                context.getString(R.string.label_music),
                ContextCompat.getColor(context, R.color.attachment_panel_category_music),
                R.drawable.ic_music_white_24dp);
    }

    public static PickerCategory file(Context context) {
        return PickerCategory.of(
                R.id.picker_category_file,
                context.getString(R.string.label_file),
                ContextCompat.getColor(context, R.color.attachment_panel_category_file),
                R.drawable.ic_file_white_24dp);
    }

    public static PickerCategory sendAsFile(Context context) {
        return PickerCategory.of(
                R.id.picker_category_send_as_file,
                context.getString(R.string.label_send_as_file),
                ContextCompat.getColor(context, R.color.attachment_panel_category_file),
                R.drawable.ic_file_white_24dp);
    }

    public static PickerCategory contact(Context context) {
        return PickerCategory.of(
                R.id.picker_category_contact,
                context.getString(R.string.label_contact),
                ContextCompat.getColor(context, R.color.attachment_panel_category_contact),
                R.drawable.ic_contact_white_24dp);
    }

    public static PickerCategory location(Context context) {
        return PickerCategory.of(
                R.id.picker_category_location,
                context.getString(R.string.label_location),
                ContextCompat.getColor(context, R.color.attachment_panel_category_location),
                R.drawable.ic_location_white_24dp);
    }

    public static PickerCategory hide(Context context) {
        return PickerCategory.of(
                R.id.picker_category_hide,
                "",
                ContextCompat.getColor(context, R.color.attachment_panel_category_hide),
                R.drawable.ic_hide_white_24dp);
    }

    public static PickerCategory send(Context context) {
        return PickerCategory.of(
                R.id.picker_category_send,
                context.getString(R.string.label_send),
                ContextCompat.getColor(context, R.color.attachment_panel_category_send),
                R.drawable.ic_send_white_24dp);
    }

    public static PickerCategory send(Context context, int count) {
        return PickerCategory.of(
                R.id.picker_category_send,
                context.getString(R.string.label_send_count, count),
                ContextCompat.getColor(context, R.color.attachment_panel_category_send),
                R.drawable.ic_send_white_24dp);
    }
}
