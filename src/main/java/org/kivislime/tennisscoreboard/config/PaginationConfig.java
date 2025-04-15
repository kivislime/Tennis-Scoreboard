package org.kivislime.tennisscoreboard.config;

public class PaginationConfig {
    private PaginationConfig() {
    }

    public static final int PAGE_SIZE = Integer.parseInt(System.getProperty("page.size", "10"));
}
