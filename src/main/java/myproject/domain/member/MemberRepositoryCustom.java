package myproject.domain.member;

import myproject.web.file.UploadFile;

public interface MemberRepositoryCustom {
    //회원 프로필 이미지 등록
    public void updateProfileById(Long id, UploadFile uploadFile);

    //회원 프로필 이미지 삭제 (새로운 이미지로 변경 시)
    void deleteProfileById(Long id, UploadFile profileImage);
}
