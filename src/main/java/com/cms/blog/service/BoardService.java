package com.cms.blog.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.blog.dto.ReplySaveRequestDto;
import com.cms.blog.model.Board;
import com.cms.blog.model.User;
import com.cms.blog.repository.BoardRepository;
import com.cms.blog.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor	//초기화 안된애들 초기화 시켜줌
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	
//	public BoardService(BoardRepository bRepo, ReplyRepository rRepo) {
//		this.boardRepository = bRepo;
//		this.replyRepository = rRepo;
//	}
	
	
//	@Autowired
//	private BoardRepository boardRepository;
//	
//	@Autowired
//	private ReplyRepository replyRepository;
	
	@Transactional
	public void 글쓰기(Board board, User user) {	//title, content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
				});
	}

	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}

	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
				});	//영속화 완료
		
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		//해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티체킹 - 자동 업데이트가 됨 . db flush
	}

	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		

//		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(()->{
//			return new IllegalArgumentException("댓글 쓰기 실패: 유저 id를 찾을 수 없습니다.");
//		});//영속화 완료
//		
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
//			return new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다.");
//		});//영속화 완료
//		
//		Reply reply = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
		
		
//		replyRepository.save(reply);
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
		
	}

	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}
	
	


		
	}

