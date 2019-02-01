package org.tvheadend.tvhclient.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.tvheadend.tvhclient.data.entity.ServerStatus;

@Dao
public interface ServerStatusDao {

    @Query("DELETE FROM server_status WHERE connection_id = :id")
    void deleteByConnectionId(int id);

    @Query("SELECT s.*, " +
            "c.name AS connection_name " +
            "FROM server_status AS s " +
            "JOIN connections AS c ON c.id = s.connection_id AND c.active = 1")
    ServerStatus loadActiveServerStatusSync();

    @Query("SELECT s.*, " +
            "c.name AS connection_name " +
            "FROM server_status AS s " +
            "JOIN connections AS c ON c.id = s.connection_id AND c.active = 1")
    LiveData<ServerStatus> loadActiveServerStatus();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ServerStatus serverStatus);

    @Update
    void update(ServerStatus serverStatus);

    @Delete
    void delete(ServerStatus serverStatus);

    @Query("DELETE FROM server_status")
    void deleteAll();

    @Query("SELECT s.*, " +
            "c.name AS connection_name " +
            "FROM server_status AS s " +
            "LEFT JOIN connections AS c ON c.id = s.connection_id " +
            "WHERE s.connection_id = :id")
    ServerStatus loadServerStatusByIdSync(int id);

    @Query("SELECT s.*, " +
            "c.name AS connection_name " +
            "FROM server_status AS s " +
            "LEFT JOIN connections AS c ON c.id = s.connection_id " +
            "WHERE s.connection_id = :id")
    LiveData<ServerStatus> loadServerStatusById(int id);
}
