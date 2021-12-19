package ntattuan.vvhieu.cuoikyltdd02.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ntattuan.vvhieu.cuoikyltdd02.App;
import ntattuan.vvhieu.cuoikyltdd02.Model.Candidate;

public class CandidateDAO extends DBManager {
    private DoneMoneyDAO doneMoneyDAO;

    public CandidateDAO(Context context) {
        super(context);
        doneMoneyDAO = new DoneMoneyDAO(context);
    }

    //Add new a cadidate
    public void addCandidate(Candidate candidate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CANDIDATE_NAME, candidate.getName());
        values.put(CANDIDATE_CMND, candidate.getCMND());
        values.put(CANDIDATE_GENDER, candidate.getGender());
        values.put(CANDIDATE_AVATAR, candidate.getAvatar());
        values.put(CANDIDATE_IS_ACTIVE, candidate.getIsActive());
        //Neu de null thi khi value bang null thi loi
        db.insert(TABLE_CANDIDATE, null, values);
        db.close();
    }

    //Check a user Exits
    public boolean CheckCandidateExits(String CMND) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CANDIDATE, new String[]{CANDIDATE_ID, CANDIDATE_NAME, CANDIDATE_CMND, CANDIDATE_GENDER, CANDIDATE_AVATAR},
                CANDIDATE_CMND + "=?",
                new String[]{CMND}, null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    //Delete a Candidate
    public void deleteCandidate(Candidate candidate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CANDIDATE, CANDIDATE_ID + " = ?",
                new String[]{String.valueOf(candidate.getId())});
        db.close();
    }

    //select ALL
    public List<Candidate> getAllCandidate() {
        List<Candidate> listCandidate = new ArrayList<Candidate>();
        String selectQuery = "SELECT  * FROM " + TABLE_CANDIDATE;
        ;
        if (App.DoanVien_Tab_Current == App.DOANVIEN_TAB_CHO_DUYET) {
            // Chọn candidate chưa active == 0
            selectQuery = "SELECT  * FROM " + TABLE_CANDIDATE + " WHERE " + CANDIDATE_IS_ACTIVE + " = " + App.NO_ACTIVE;
        }
        if (App.DoanVien_Tab_Current == App.DOANVIEN_TAB_DOAN_PHI || App.DoanVien_Tab_Current == App.DOANVIEN_TAB_HOI_PHI) {
            // Chọn candidate chưa active == 0
            selectQuery = "SELECT  * FROM " + TABLE_CANDIDATE + " WHERE " + CANDIDATE_IS_ACTIVE + " = " + App.ACTIVE;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Candidate candidate = new Candidate();
                candidate.setId(cursor.getInt(0));
                candidate.setName(cursor.getString(1));
                candidate.setCMND(cursor.getString(2));
                candidate.setGender(cursor.getInt(3));
                candidate.setAvatar(cursor.getBlob(4));
                candidate.setIsActive(cursor.getInt(5));
                //Kiểm tra nạp đoàn phí/Hội phí hay chưa?
                if (doneMoneyDAO.checkExits(candidate.getId(), App.DotNopTienDoanPhi_Current)) {
                    candidate.setDoanPhi(true);
                }
                if (doneMoneyDAO.checkExits(candidate.getId(), App.DotNopTienHoiPhi_Current)) {
                    candidate.setHoiPhi(true);
                }
                listCandidate.add(candidate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listCandidate;
    }

    //select by id
    public Candidate getCandidateByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CANDIDATE, new String[]{CANDIDATE_ID, CANDIDATE_NAME, CANDIDATE_CMND, CANDIDATE_GENDER, CANDIDATE_AVATAR},
                CANDIDATE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Candidate candidate = new Candidate();
        if (cursor.moveToFirst()) {
            candidate.setId(cursor.getInt(0));
            candidate.setName(cursor.getString(1));
            candidate.setCMND(cursor.getString(2));
            candidate.setGender(cursor.getInt(3));
            candidate.setAvatar(cursor.getBlob(4));
        }
        cursor.close();
        db.close();
        return candidate;
    }

    //select by name
    public List<Candidate> getCandidateByName(String name) {
        List<Candidate> listCandidate = new ArrayList<Candidate>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (App.DoanVien_Tab_Current == App.DOANVIEN_TAB_CHO_DUYET) {
            cursor = db.query(TABLE_CANDIDATE, new String[]{CANDIDATE_ID, CANDIDATE_NAME, CANDIDATE_CMND, CANDIDATE_GENDER, CANDIDATE_AVATAR, CANDIDATE_IS_ACTIVE},
                    CANDIDATE_NAME + " LIKE ? AND WHERE " + CANDIDATE_IS_ACTIVE + " = ? COLLATE NOCASE",
                    new String[]{"%" + name + "%", String.valueOf(App.NO_ACTIVE)}, null, null, null, null);
        } else if (App.DoanVien_Tab_Current == App.DOANVIEN_TAB_ALL) {
            cursor = db.query(TABLE_CANDIDATE, new String[]{CANDIDATE_ID, CANDIDATE_NAME, CANDIDATE_CMND, CANDIDATE_GENDER, CANDIDATE_AVATAR, CANDIDATE_IS_ACTIVE},
                    CANDIDATE_NAME + " LIKE ? COLLATE NOCASE",
                    new String[]{"%" + name + "%"}, null, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do {
                Candidate candidate = new Candidate();
                candidate.setId(cursor.getInt(0));
                candidate.setName(cursor.getString(1));
                candidate.setCMND(cursor.getString(2));
                candidate.setGender(cursor.getInt(3));
                candidate.setAvatar(cursor.getBlob(4));
                candidate.setIsActive(cursor.getInt(5));
                //Kiểm tra nạp đoàn phí/Hội phí hay chưa?
                if (doneMoneyDAO.checkExits(candidate.getId(), App.DotNopTienDoanPhi_Current)) {
                    candidate.setDoanPhi(true);
                }
                if (doneMoneyDAO.checkExits(candidate.getId(), App.DotNopTienHoiPhi_Current)) {
                    candidate.setHoiPhi(true);
                }
                listCandidate.add(candidate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listCandidate;
    }

    public void ActiveCandidate(Candidate candidate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CANDIDATE_IS_ACTIVE, App.ACTIVE);
        db.update(
                TABLE_CANDIDATE,
                values,
                CANDIDATE_ID + "=?",
                new String[]{String.valueOf(candidate.getId())
                });
    }
}