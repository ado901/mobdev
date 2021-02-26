package Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Categorie.class,
        parentColumns = "categorie",
        childColumns = "category",
        onDelete = CASCADE))
public class Expenses {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "amount")
    private Float amount;

    @ColumnInfo(name = "currency")
    private String currency;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "date")
    private Integer date;

    @ColumnInfo(name = "note")
    private String note;

    public Expenses(Float amount, String currency, String category, Integer date, String note) {
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.date = date;
        this.note = note;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
