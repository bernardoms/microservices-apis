package com.bernardoms.campaign.mapper;

public interface Mapper<Doc,Resp> {
    Resp mapToResp(Doc mappedDocument);
    Doc  mapToDocument(Resp mappedResponse);

}
