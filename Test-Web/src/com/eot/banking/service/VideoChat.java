package com.eot.banking.service;

import com.opentok.OpenTok;
import com.opentok.MediaMode;
import com.opentok.ArchiveMode;
import com.opentok.Session;
import com.opentok.SessionProperties;
import com.opentok.exception.OpenTokException;
import com.opentok.TokenOptions;
import com.opentok.Role;

import java.util.List;

import com.opentok.Archive;
import com.opentok.ArchiveProperties;

public class VideoChat {

	int apiKey = 45704962; // YOUR API KEY
	String apiSecret = "9f835d69aa0c8d2d024da6b8bcb40c071a432dd1";
	OpenTok opentok = new OpenTok(apiKey, apiSecret);

	public String createVideoSession() throws OpenTokException{

		/* A session that attempts to stream media directly between clients : */
		Session session = opentok.createSession();

		/* A session that uses the OpenTok Media Router (which is required for archiving): */

		//		session = opentok.createSession(new SessionProperties.Builder()
		//				.mediaMode(MediaMode.ROUTED)
		//				.build());
		//

		/*A Session with a location hint:*/

		//		session = opentok.createSession(new SessionProperties.Builder()
		//				.location("12.34.56.78")
		//				.build());
		//

		/*A session that is automatically archived (it must used the routed media mode)*/

		//		session = opentok.createSession(new SessionProperties.Builder()
		//				.mediaMode(MediaMode.ROUTED)
		//				.archiveMode(ArchiveMode.ALWAYS)
		//				.build());

		/* Store this sessionId in the database for later use:*/

		String sessionId = session.getSessionId();

		/* Generate a token from just a sessionId (fetched from a database) */
		String token = opentok.generateToken(sessionId);

		/* Generate a token by calling the method on the Session (returned from createSession) */
		//		String token = session.generateToken();
		//

		/*Set some options in a token*/

		//		String token = session.generateToken(new TokenOptions.Builder()
		//		  .role(Role.MODERATOR)
		//		  .expireTime((System.currentTimeMillis() / 1000L) + (7 * 24 * 60 * 60)) // in one week
		//		  .data("name=Johnny")
		//		  .build());

		/*A simple Archive (without a name)*/
		
		//		 Archive archive = opentok.startArchive(sessionId, null);
		//
		//		 // Store this archiveId in the database for later use
		//		 String archiveId = archive.getId();

		/*Start an audio-only archive*/
		
		//		Archive archive = opentok.startArchive(sessionId, new ArchiveProperties.Builder()
		//		  .hasVideo(false)
		//		  .build());
		//
		/* Store this archiveId in the database for later use */

		//		String archiveId = archive.getId();

		//		Archive archive = opentok.startArchive(sessionId, new ArchiveProperties.Builder()
		//				  .archiveMode(Archive.OutputMode.INDIVIDUAL)
		//				  .build()););
		//
		
		/*Store this archiveId in the database for later use*/
		
		//				String archiveId = archive.getId();

		/* Stop an Archive from an archiveId (fetched from database) */

		//	Archive archive = opentok.stopArchive(archiveId);

		// Delete an Archive from an archiveId (fetched from database)
		// opentok.deleteArchive(archiveId);

		/* Get a list with the first 1000 archives created by the API Key */

		//		List<Archive> archives = opentok.listArchives();
		//
		//		// Get a list of the first 50 archives created by the API Key
		//		List<Archive> archives = opentok.listArchives(0, 50);
		//
		//		// Get a list of the next 50 archives
		//		List<Archive> archives = opentok.listArchives(50, 50);
		
		return token;
	}
	
	public static void main(String[] args) {
		
		VideoChat videoChat = new VideoChat();
		try {
			System.out.println(videoChat.createVideoSession());
		} catch (OpenTokException e) {
			e.printStackTrace();
		}
	}
}
