package com.rebirth.simplepost.utils.listeners;

import com.rebirth.simplepost.domain.tables.records.CommentsRecord;
import com.rebirth.simplepost.domain.tables.records.PostsRecord;
import com.rebirth.simplepost.domain.tables.records.TagsRecord;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.impl.DefaultRecordListener;

import java.time.LocalDateTime;

public class AuditListener extends DefaultRecordListener {


    @Override
    public void insertStart(RecordContext ctx) {
        Record record = ctx.record();
        if (record instanceof TagsRecord) {
            TagsRecord tagsRecord = (TagsRecord) record;
            tagsRecord.setCreateAt(LocalDateTime.now());
            tagsRecord.setUpdateAt(LocalDateTime.now());
            tagsRecord.setCreateBy("FROM SPRINGBOOT");
            tagsRecord.setUpdateBy("FROM SPRINGBOOT");
            tagsRecord.setVersion(1L);
        } else if (record instanceof CommentsRecord) {
            CommentsRecord commentRecord = (CommentsRecord) record;
            commentRecord.setCreateAt(LocalDateTime.now());
            commentRecord.setUpdateAt(LocalDateTime.now());
            commentRecord.setCreateBy("FROM SPRINGBOOT");
            commentRecord.setUpdateBy("FROM SPRINGBOOT");
            commentRecord.setVersion(1L);
        } else if (record instanceof PostsRecord) {
            PostsRecord postRecord = (PostsRecord) record;
            postRecord.setCreateAt(LocalDateTime.now());
            postRecord.setUpdateAt(LocalDateTime.now());
            postRecord.setCreateBy("FROM SPRINGBOOT");
            postRecord.setUpdateBy("FROM SPRINGBOOT");
            postRecord.setVersion(1L);
        }
    }

    @Override
    public void updateStart(RecordContext ctx) {
        Record record = ctx.record();
        if (record instanceof TagsRecord) {
            TagsRecord tagsRecord = (TagsRecord) record;
            tagsRecord.setUpdateAt(LocalDateTime.now());
            tagsRecord.setUpdateBy("FROM SPRINGBOOT");
            tagsRecord.setVersion(tagsRecord.getVersion() + 1);
        } else if (record instanceof CommentsRecord) {
            CommentsRecord commentRecord = (CommentsRecord) record;
            commentRecord.setUpdateAt(LocalDateTime.now());
            commentRecord.setUpdateBy("FROM SPRINGBOOT");
            commentRecord.setVersion(commentRecord.getVersion() + 1);
        } else if (record instanceof PostsRecord) {
            PostsRecord postRecord = (PostsRecord) record;
            postRecord.setUpdateAt(LocalDateTime.now());
            postRecord.setUpdateBy("FROM SPRINGBOOT");
            postRecord.setVersion(postRecord.getVersion() + 1);
        }
    }
}
