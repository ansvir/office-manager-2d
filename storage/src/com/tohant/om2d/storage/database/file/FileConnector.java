package com.tohant.om2d.storage.database.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tohant.om2d.storage.database.file.model.FileTable;

public interface FileConnector {

    FileTable connect(String dbPath);

}
