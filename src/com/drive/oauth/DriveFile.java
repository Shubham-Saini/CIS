package com.drive.oauth;

public class DriveFile 
{
	private String fileName;
	private String folderName;
	private String mimeType;
	private String downloadLink;
	private boolean isFile;
	private boolean isRoot;
	private String parentId;
	private String fileId;
	private String server;
	private String lastModified;
	private String uname;
	public DriveFile(String fileName, String folderName, String mimeType,
			String downloadLink, boolean isFile, boolean isRoot,
			String parentId, String fileId,String server,String lastModified,String uname) {
		super();
		this.fileName = fileName;
		this.folderName = folderName;
		this.mimeType = mimeType;
		this.downloadLink = downloadLink;
		this.isFile = isFile;
		this.isRoot = isRoot;
		this.parentId = parentId;
		this.fileId = fileId;
		this.server = server;
		this.lastModified = lastModified;
		this.uname = uname;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	public String getDownloadLink() {
		return downloadLink;
	}
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	public boolean isFile() {
		return isFile;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
//	public String getParentName() {
//		return parentName;
//	}
//	public void setParentName(String parentName) {
//		this.parentName = parentName;
//	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	
}
