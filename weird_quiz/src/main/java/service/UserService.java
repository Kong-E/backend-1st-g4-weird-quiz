package service;

import java.sql.Connection;

import constants.ExceptionMessage;
import dao.UserDAO;
import model.User;

public class UserService {
	private final Connection connection;
	private final UserDAO userDAO;

	public UserService(Connection connection) {
		this.connection = connection;
		userDAO = new UserDAO(connection);
	}

	public User login(String userId, String password) {

		User user = userDAO.findById(userId);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		if (user != null) {
			throw new RuntimeException(ExceptionMessage.WRONG_PASSWORD.getMessage());
		}
		throw new RuntimeException(ExceptionMessage.NOT_FOUND_USER.getMessage());
	}

	public User signup(String userId, String password, String userName) {
		// 중복된 유저 체크
		User existingUser = userDAO.findById(userId);
		if (existingUser != null) {
			throw new RuntimeException(ExceptionMessage.EXIST_USER_ID.getMessage());
		}

		// 새로운 유저 저장
		User newUser = new User(userId, password, userName, 0, 0, 0, 0);

		return userDAO.save(newUser);
	}

	public void gameStart(User user) {
		user.setGameAttemptCount(user.getGameAttemptCount() + 1);
	}

	public void solvedQuiz(User user) {
		user.setQuizSolvedCount(user.getQuizSolvedCount() + 1);
	}

	public void correctQuiz(User user) {
		user.setQuizCorrectCount(user.getQuizCorrectCount() + 1);
	}

	public void successQuiz(User user) {
		user.setGameSuccessCount(user.getGameSuccessCount() + 1);
	}

	public void save(User user) { // 게임 정보 저장
		userDAO.updateById(user);
	}

	public double calSuccessRate(User user) {
		// 성공률 계산
        if (user.getGameAttemptCount() > 0) {
            return (double) user.getGameSuccessCount() / user.getGameAttemptCount() * 100;
        }
        return 0;
    }

  
    public double calCorrectAnswerRate(User user) {
		  // 정답률 계산
        if (user.getQuizSolvedCount() > 0) {
            return (double) user.getQuizCorrectCount() / user.getQuizSolvedCount() * 100;
        }
        return 0;
    }

}
