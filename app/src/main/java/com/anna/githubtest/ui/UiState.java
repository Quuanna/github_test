package com.anna.githubtest.ui;

public abstract class UiState {

    private UiState() {}

    public static final class Loading extends UiState {
        private Loading() {}
        public static Loading getInstance() {
            return new Loading();
        }
    }

    public static final class Success extends UiState {
        private Success() {}
        public static Success getInstance() {
            return new Success();
        }
    }

    public static final class Error extends UiState {
        private final String message;

        public Error() {
            this("Network Request failed");
        }

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
