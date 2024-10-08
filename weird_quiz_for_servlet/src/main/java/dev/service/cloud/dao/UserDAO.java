package dev.service.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.service.cloud.model.User;
import dev.service.cloud.util.DBUtil;

public class UserDAO {
	public User findById(String id) {
		final String selectQuery = "SELECT * FROM weird_quiz.users WHERE user_id = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(selectQuery)) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String userId = resultSet.getString("user_id");
					String password = resultSet.getString("password");
					String userName = resultSet.getString("user_name");
					long gameAttemptCount = resultSet.getLong("game_attempt_count");
					long gameSuccessCount = resultSet.getLong("game_success_count");
					long quizSolvedCount = resultSet.getLong("quiz_solved_count");
					long quizCorrectCount = resultSet.getLong("quiz_correct_count");

					// DB에서 받아온 데이터를 Todo 모델 객체로 바인딩
					return new User(userId, password, userName, gameAttemptCount, gameSuccessCount, quizSolvedCount,
							quizCorrectCount);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public User save(User user) {
		final String insertQuery = "INSERT INTO weird_quiz.users(user_id, password, user_name) VALUES (?, ?, ?)";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(insertQuery)) {
			statement.setString(1, user.getUserId());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getUserName());

			statement.executeUpdate();
			return user; // 삽입된 유저 객체를 반환
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateById(User user) {
		final String query = "UPDATE users SET game_attempt_count = ?, game_success_count = ?, quiz_solved_count = ?, quiz_correct_count = ? WHERE user_id = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, user.getGameAttemptCount());
			statement.setLong(2, user.getGameSuccessCount());
			statement.setLong(3, user.getQuizSolvedCount());
			statement.setLong(4, user.getQuizCorrectCount());
			statement.setString(5, user.getUserId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
