package com.cinema.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cinema.entity.Hall;
import com.cinema.entity.HallSeat;
import com.cinema.mapper.HallMapper;
import com.cinema.mapper.HallSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {
    
    private final HallMapper hallMapper;
    private final HallSeatMapper hallSeatMapper;
    
    public List<Hall> getHallList() {
        return hallMapper.selectList(null);
    }
    
    public Hall getHallDetail(Long id) {
        return hallMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void createHall(Hall hall) {
        hall.setTotalSeats(hall.getRows() * hall.getCols());
        hallMapper.insert(hall);
        
        // 自动生成座位布局
        for (int row = 1; row <= hall.getRows(); row++) {
            for (int col = 1; col <= hall.getCols(); col++) {
                HallSeat seat = new HallSeat();
                seat.setHallId(hall.getId());
                seat.setRowNum(row);
                seat.setColNum(col);
                seat.setSeatType("normal");
                hallSeatMapper.insert(seat);
            }
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateHall(Hall hall) {
        Hall oldHall = hallMapper.selectById(hall.getId());
        if (oldHall == null) {
            throw new RuntimeException("影厅不存在");
        }
        
        hall.setTotalSeats(hall.getRows() * hall.getCols());

        if (!oldHall.getRows().equals(hall.getRows()) || !oldHall.getCols().equals(hall.getCols())) {
            // 删除旧座位
            LambdaQueryWrapper<HallSeat> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HallSeat::getHallId, hall.getId());
            hallSeatMapper.delete(wrapper);
            
            // 生成新座位
            for (int row = 1; row <= hall.getRows(); row++) {
                for (int col = 1; col <= hall.getCols(); col++) {
                    HallSeat seat = new HallSeat();
                    seat.setHallId(hall.getId());
                    seat.setRowNum(row);
                    seat.setColNum(col);
                    seat.setSeatType("normal");
                    hallSeatMapper.insert(seat);
                }
            }
        }
        
        hallMapper.updateById(hall);
    }
    
    public void deleteHall(Long id) {
        // 删除影厅座位
        LambdaQueryWrapper<HallSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HallSeat::getHallId, id);
        hallSeatMapper.delete(wrapper);
        
        // 删除影厅
        hallMapper.deleteById(id);
    }
}
