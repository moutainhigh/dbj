package com.zwdbj.server.adminserver.service.shop.service.customerComments.service;

import com.zwdbj.server.adminserver.service.shop.service.customerComments.mapper.CustomerCommentMapper;
import com.zwdbj.server.adminserver.service.shop.service.customerComments.model.CommentInfo;
import com.zwdbj.server.adminserver.service.shop.service.customerComments.model.CommentRank;
import com.zwdbj.server.adminserver.service.shop.service.customerComments.model.ReplyComment;
import com.zwdbj.server.adminserver.service.shop.service.customerComments.model.UserComments;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCommentServiceImpl implements CustomerCommentService {

    @Autowired
    private CustomerCommentMapper customerCommentMapper;

    @Override
    public ServiceStatusInfo<List<CommentInfo>> commentList(long legalSubjectId) {
        List<CommentInfo> result = null;
        try {
            result = this.customerCommentMapper.commentList(legalSubjectId);
            for (CommentInfo c : result) {
                c.setRefComment(this.customerCommentMapper.commentReply(c.getId()));
            }
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "拉取用户评论失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Long> replyComment(ReplyComment replyComment) {
        try {
            long id = UniqueIDCreater.generateID();
            long result = this.customerCommentMapper.replyComment(id, replyComment);

            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "回复评论失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Long> deleteComment(long commentId) {
        try {

            long result = this.customerCommentMapper.deleteComment(commentId);

            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "删除评论失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<UserComments> countComments(long legalSubjectId) {
        try {
            UserComments result = this.customerCommentMapper.countComments(legalSubjectId);

            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "统计用户评价失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<List<CommentInfo>> commentRankList(long legalSubjectId, float rate) {
        try {
            List<CommentInfo> result = this.customerCommentMapper.commentRankList(legalSubjectId, rate);

            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "分级拉取用户评论失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo< List<CommentRank>> commentRank(long legalSubjectId) {
        try {
            List<CommentRank> result = this.customerCommentMapper.commentRank(legalSubjectId);

            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "分级统计用户评论失败" + e.getMessage(), null);
        }
    }
}