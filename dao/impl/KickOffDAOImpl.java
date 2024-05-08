package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IKickOffDAO;
import com.bh.realtrack.dto.KickOffDTO;
import com.bh.realtrack.dto.KickOffLogDetailsDTO;
import com.bh.realtrack.dto.SaveKickOffResponseDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.KickOffConstants;

/**
 *
 * @author Anand Kumar
 *
 */
@Repository
public class KickOffDAOImpl implements IKickOffDAO {
	private static Logger log = LoggerFactory.getLogger(KickOffDAOImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int saveKickOffData(final String projectId, final String matrix, final String sso) {
		int result = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement deletePstm = con.prepareStatement(KickOffConstants.DELETE_KICK_OFF_DETAILS);
			// PreparedStatement deletePstm = jdbcTemplate.getDataSource().getConnection()
			// .prepareStatement(KickOffConstants.DELETE_KICK_OFF_DETAILS);
			deletePstm.setString(1, projectId);

			if (deletePstm.executeUpdate() >= 0) {
				PreparedStatement savePstm = con.prepareStatement(KickOffConstants.SAVE_KICK_OFF_DETAILS);
				// PreparedStatement savePstm = jdbcTemplate.getDataSource().getConnection()
				// .prepareStatement(KickOffConstants.SAVE_KICK_OFF_DETAILS);

				savePstm.setString(1, projectId);
				savePstm.setString(2, matrix);
				savePstm.setString(3, sso);
				savePstm.setString(4, sso);

				if (savePstm.executeUpdate() > 0) {
					result = 1;
				}
			}
		} catch (Exception e) {
			log.error("something went wrong while deleting or saving kickoff details:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public List<KickOffDTO> getKickOffData(final String projectId) {
		return jdbcTemplate.query(KickOffConstants.GET_KICK_OFF_DETAILS, new Object[] { projectId },
				new ResultSetExtractor<List<KickOffDTO>>() {
					@Override
					public List<KickOffDTO> extractData(final ResultSet rs) {
						List<KickOffDTO> list = new ArrayList<KickOffDTO>();
						try {
							while (rs.next()) {
								KickOffDTO kickOffDTO = new KickOffDTO();
								kickOffDTO.setProjectId(rs.getLong(1));
								kickOffDTO.setMatrix(rs.getString(2));
								list.add(kickOffDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting kickoff details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public KickOffLogDetailsDTO getKickOffLogDetails(final String projectId) {
		return jdbcTemplate.query(KickOffConstants.SAVE_KICK_OFF_LOG_DETAILS, new Object[] { projectId },
				new ResultSetExtractor<KickOffLogDetailsDTO>() {
					@Override
					public KickOffLogDetailsDTO extractData(final ResultSet rs) {
						KickOffLogDetailsDTO kickOffLogDetailsDTO = new KickOffLogDetailsDTO();
						try {
							while (rs.next()) {
								kickOffLogDetailsDTO.setFirstName(rs.getString(1));
								kickOffLogDetailsDTO.setLastName(rs.getString(2));
								kickOffLogDetailsDTO.setLastUpdatedDate(rs.getString(3));
							}
						} catch (SQLException e) {
							log.error("something went wrong while saving kickoff log details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return kickOffLogDetailsDTO;
					}
				});
	}

	@Override
	public SaveKickOffResponseDTO getKickOffSaveLogDetails(final String projectId) {
		return jdbcTemplate.query(KickOffConstants.SAVE_KICK_OFF_LOG_DETAILS, new Object[] { projectId },
				new ResultSetExtractor<SaveKickOffResponseDTO>() {
					@Override
					public SaveKickOffResponseDTO extractData(final ResultSet rs) {
						SaveKickOffResponseDTO saveKickOffResponseDTO = new SaveKickOffResponseDTO();
						try {
							while (rs.next()) {
								saveKickOffResponseDTO.setFirstName(rs.getString(1));
								saveKickOffResponseDTO.setLastName(rs.getString(2));
								saveKickOffResponseDTO.setLastSaveDate(rs.getString(3));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting kickoff log details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return saveKickOffResponseDTO;
					}
				});
	}

	@Override
	public KickOffDTO getMatrixDetails(final String projectId) {
		return jdbcTemplate.query(KickOffConstants.GET_KICK_OFF_DETAILS, new Object[] { projectId },
				new ResultSetExtractor<KickOffDTO>() {
					@Override
					public KickOffDTO extractData(final ResultSet rs) {
						KickOffDTO kickOffMatrixDTO = new KickOffDTO();
						try {
							while (rs.next()) {
								kickOffMatrixDTO.setMatrix(rs.getString(2));
							}
						} catch (SQLException e) {
							log.error("Error in getting kickoff matrix details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return kickOffMatrixDTO;
					}
				});
	}

}
