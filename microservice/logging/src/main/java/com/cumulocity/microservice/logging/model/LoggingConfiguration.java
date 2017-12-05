package com.cumulocity.microservice.logging.model;

public class LoggingConfiguration {
    private String directory;
    private String file;

    public LoggingConfiguration() {
    }

    public String getDirectory() {
        return this.directory;
    }

    public String getFile() {
        return this.file;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof LoggingConfiguration)) return false;
        final LoggingConfiguration other = (LoggingConfiguration) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$directory = this.getDirectory();
        final Object other$directory = other.getDirectory();
        if (this$directory == null ? other$directory != null : !this$directory.equals(other$directory)) return false;
        final Object this$file = this.getFile();
        final Object other$file = other.getFile();
        if (this$file == null ? other$file != null : !this$file.equals(other$file)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $directory = this.getDirectory();
        result = result * PRIME + ($directory == null ? 43 : $directory.hashCode());
        final Object $file = this.getFile();
        result = result * PRIME + ($file == null ? 43 : $file.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoggingConfiguration;
    }

    public String toString() {
        return "LoggingConfiguration(directory=" + this.getDirectory() + ", file=" + this.getFile() + ")";
    }
}
