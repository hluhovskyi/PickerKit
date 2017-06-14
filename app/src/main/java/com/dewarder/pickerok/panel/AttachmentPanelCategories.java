package com.dewarder.pickerok.panel;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dewarder.pickerok.R;

public final class AttachmentPanelCategories {

    public static AttachmentPanelCategory camera(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_camera,
                context.getString(R.string.label_camera),
                ContextCompat.getColor(context, R.color.attachment_panel_category_camera),
                R.drawable.ic_camera_white_24dp);
    }

    public static AttachmentPanelCategory gallery(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_gallery,
                context.getString(R.string.label_gallery),
                ContextCompat.getColor(context, R.color.attachment_panel_category_gallery),
                R.drawable.ic_gallery_white_24dp);
    }

    public static AttachmentPanelCategory video(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_video,
                context.getString(R.string.label_video),
                ContextCompat.getColor(context, R.color.attachment_panel_category_video),
                R.drawable.ic_video_white_24dp);
    }

    public static AttachmentPanelCategory music(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_music,
                context.getString(R.string.label_music),
                ContextCompat.getColor(context, R.color.attachment_panel_category_music),
                R.drawable.ic_music_white_24dp);
    }

    public static AttachmentPanelCategory file(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_file,
                context.getString(R.string.label_file),
                ContextCompat.getColor(context, R.color.attachment_panel_category_file),
                R.drawable.ic_file_white_24dp);
    }

    public static AttachmentPanelCategory sendAsFile(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_send_as_file,
                context.getString(R.string.label_send_as_file),
                ContextCompat.getColor(context, R.color.attachment_panel_category_file),
                R.drawable.ic_file_white_24dp);
    }

    public static AttachmentPanelCategory contact(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_contact,
                context.getString(R.string.label_contact),
                ContextCompat.getColor(context, R.color.attachment_panel_category_contact),
                R.drawable.ic_contact_white_24dp);
    }

    public static AttachmentPanelCategory location(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_location,
                context.getString(R.string.label_location),
                ContextCompat.getColor(context, R.color.attachment_panel_category_location),
                R.drawable.ic_location_white_24dp);
    }

    public static AttachmentPanelCategory hide(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_hide,
                "",
                ContextCompat.getColor(context, R.color.attachment_panel_category_hide),
                R.drawable.ic_hide_white_24dp);
    }

    public static AttachmentPanelCategory send(Context context) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_send,
                context.getString(R.string.label_send),
                ContextCompat.getColor(context, R.color.attachment_panel_category_send),
                R.drawable.ic_send_white_24dp);
    }

    public static AttachmentPanelCategory send(Context context, int count) {
        return AttachmentPanelCategory.of(
                R.id.attachment_panel_category_send,
                context.getString(R.string.label_send_count, count),
                ContextCompat.getColor(context, R.color.attachment_panel_category_send),
                R.drawable.ic_send_white_24dp);
    }
}
