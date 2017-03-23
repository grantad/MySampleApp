package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "comicgeek-mobilehub-396237625-Comics")

public class ComicsDO {
    private String _userId;
    private String _bookTitle;
    private Double _issueNumber;
    private String _publicationDate;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "Book_title")
    public String getBookTitle() {
        return _bookTitle;
    }

    public void setBookTitle(final String _bookTitle) {
        this._bookTitle = _bookTitle;
    }
    @DynamoDBAttribute(attributeName = "issue_number")
    public Double getIssueNumber() {
        return _issueNumber;
    }

    public void setIssueNumber(final Double _issueNumber) {
        this._issueNumber = _issueNumber;
    }
    @DynamoDBAttribute(attributeName = "publication_date")
    public String getPublicationDate() {
        return _publicationDate;
    }

    public void setPublicationDate(final String _publicationDate) {
        this._publicationDate = _publicationDate;
    }

}
