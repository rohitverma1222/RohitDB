import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class RohitDB {
    public static void main(String[] args) throws IOException {
        UserPrompt userInput = new UserPrompt();
        userInput.takeUserInput();
    }
}

class UserPrompt {
    enum MetaCommandResult {
        META_COMMAND_SUCCESS,
        META_COMMAND_UNRECOGNIZED;
    }

    enum PrepareResult {
        PREPARE_SUCCESS,
        PREPARE_UNRECOGNIZED_STATEMENT,
        PREPARE_SYNTAX_ERROR,
    }

    enum StatementType {
        STATEMENT_INSERT,
        STATEMENT_SELECT;
    }

    class Row {
        int id;
        String username;
        String email;
    }

    class Statement {
        StatementType type;
        Row row_to_insert;
    }

    MetaCommandResult do_meta_command(String userInput) {
        // String userInput = reader.readLine();
        if (userInput != null && userInput.equals(".exit")) {
            return MetaCommandResult.META_COMMAND_SUCCESS;
        } else {
            return MetaCommandResult.META_COMMAND_UNRECOGNIZED;
        }
    }

    PrepareResult prepare_statement(String userInput, Statement statement) throws IOException {
        // String userInput = reader.readLine();
        if ("insert".equals(userInput.substring(0, 6))) {
            String responseValues[] = userInput.trim().split("\\s+");

            if (!isNumeric(responseValues[1]) || responseValues.length<4) {
                return PrepareResult.PREPARE_SYNTAX_ERROR;
            }
            statement.row_to_insert.id = Integer.parseInt(responseValues[1]);
            statement.row_to_insert.username = responseValues[2];
            statement.row_to_insert.email = responseValues[3];

            statement.type = StatementType.STATEMENT_INSERT;
            return PrepareResult.PREPARE_SUCCESS;
        }
        if (userInput.equals("select")) {
            statement.type = StatementType.STATEMENT_SELECT;
            return PrepareResult.PREPARE_SUCCESS;
        }
        return PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT;
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void takeUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            print_prompt();
            String userInput = reader.readLine();

            if (userInput.charAt(0) == '.') {
                switch (do_meta_command(userInput)) {
                    case META_COMMAND_SUCCESS:
                        continue;
                    case META_COMMAND_UNRECOGNIZED:
                        System.out.println("Unrecognized command : " + userInput);
                        continue;
                }
            }

            Statement statement = new Statement();

            switch (prepare_statement(userInput, statement)) {
                case PREPARE_SUCCESS:
                    break;
                case PREPARE_UNRECOGNIZED_STATEMENT:
                    System.out.println("Unrecognized keyword at start of : " + userInput);
                    continue;
                default:
                    break;
            }
            execute_statement(statement);
            System.out.println("Executed");
        }
    }

    public void execute_statement(Statement statement) {
        switch (statement.type) {
            case STATEMENT_INSERT:
                System.out.println("This is where we would do an insert.");
                break;
            case STATEMENT_SELECT:
                System.out.println("This is where we would do a select.");
                break;
        }
    }

    public void print_prompt() {
        System.out.print("DB > ");
    }
}