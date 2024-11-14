package com.mobigen.datalake.post;

import java.util.List;
import java.util.Map;

public interface PostMapper {
    List getPost(String type, String word, String postId) throws Exception;
    List getAllPostID() throws Exception;
    List getAllPost(String postType) throws Exception;

    List getPostList(Map<String, String> parma) throws Exception;
    Integer getPostListTotal(Map<String, String> parma) throws Exception;

    Map<String, Object> getPostOne(String postId) throws Exception;
    void updateViewCount(String postId) throws Exception;
    Integer insertPost(Map<String, Object> param) throws Exception;
    Integer updatePost(Map<String, Object> param) throws Exception;
    Integer updatePostFaqOrdNo(Map<String, Object> param) throws Exception;

    Integer deletePost(Integer id, String post_type) throws Exception;

    List getMailUserList() throws Exception;
}
