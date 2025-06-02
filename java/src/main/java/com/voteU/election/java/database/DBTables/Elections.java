    package com.voteU.election.java.database.DBTables;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import lombok.Getter;
    import lombok.Setter;
    import jakarta.persistence.Transient;


    @Getter
    @Setter
    @Entity
    @Table(name = "elections")
    public class Elections {
        @Id
        @Column(name = "id")
        String id;

        String name;

        String date;

        int votes;

        @Transient
        int partiesSize;

        public Elections(String stringId, String name, int votes, int partiesSize){
            this.id = stringId;
            this.name = name;
            this.votes = votes;
            this.partiesSize = partiesSize;
        }

        public Elections() {

        }
    }
