package com.alant7_.util.data;

public class DataManager <DataSource extends IDataSource, DataLoader extends IDataLoader> {

    private final DataLoader dataLoader;

    private final DataSource dataSource;

    public DataManager(DataSource source, DataLoader loader) {
        this.dataSource = source;
        this.dataLoader = loader;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }

}
